package com.example.group8.reciperescue;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 *  Created by Moses Chuang.
 *
 *  This is the main activity of Yummly API.
 *  This activity has one search textbox for recipe name, and one for ingredient
 *  This activity sends http request to Yummly and parses the retrun value.
 *
 * @author Moses Chuang
 */

public class YummlyActivity extends Activity {

    public final static String apiURL = "http://api.yummly.com/v1/api/recipes?";
    public final static String YUMMLY_ID = "46c3f44a";
    public final static String YUMMLY_KEY = "d0e13b5d6a6cf024d5b3ce9b5ea8e99e";
    public final static String EXTRA_MESSAGE = "YummlyVector";
    public Vector<RecipeMessageObject> vRMO;

    /**
     * Initializes the components for the Yummly actions.
     * @param savedInstanceState Bundle object, state required to restore previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String dummy = getIntent().getStringExtra("q");
        query(dummy);

    }

    /**
     * This is the search function for Yummly API,
     * It build search URL from the query string
     * And pass the URL to SearchAPI()
     * @param q, String, the query input string
     */
    public void query(String q) {
        String a[] = q.split(",");
        String urlString = apiURL + "_app_id=" + YUMMLY_ID + "&_app_key=" + YUMMLY_KEY;
        for (int i = 0; i < a.length; i++) {
             String ingr = a[i].toLowerCase();
             urlString = urlString.concat("&allowedIngredient[]=" + ingr);

        }
        System.out.println(urlString);
        new SearchAPI().execute(urlString);
    }

    /**
     *  This is an AsyncTask to call Yummly API
     *  When it is executed, it will call doInBackground(), then call onPostExcute()
     *
     *  @author Moses Chuang
     */
    private class SearchAPI extends AsyncTask<String, String, Vector<RecipeMessageObject>> {


        /**
         *  This function will be called when SearchAPI.execute()
         *  In this function, using the URL to get information from Yummlu Database,
         *  And parse the returned JSON format, extract the field we want.
         *
         *  @param params, String..., The URL String passed when CallAPI task is executed
         *  @return Vector<RecipeMessageObject>, the result vector we parsed from JSON
         */
        @Override
        protected Vector<RecipeMessageObject> doInBackground(String... params) {

            String urlString = params[0]; // URL to call

            String result = null;
            vRMO = new Vector<RecipeMessageObject>();


            result = parseURL(urlString);

            try {
                // Parse the JSON format
                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.getJSONArray("matches");
                StringBuilder str = new StringBuilder();
                for(int i = 0; i < jArray.length() && i < 5; i++) {
                    JSONObject recipe = jArray.getJSONObject(i);
                    String id = recipe.getString("id");
                    String urlStringGet = "http://api.yummly.com/v1/api/recipe/" + id + "?_app_id=" + YUMMLY_ID + "&_app_key=" + YUMMLY_KEY;

                    String resultGet = parseURL(urlStringGet);
                    JSONObject jObjectGet = new JSONObject(resultGet);
                    JSONObject source = jObjectGet.getJSONObject("source");
                    String sourceURL = source.getString("sourceRecipeUrl");

                    String name = recipe.getString("recipeName");
                    JSONArray images = jObjectGet.getJSONArray("images");
                    JSONObject image = images.getJSONObject(0);
                    String imgURL = image.getString("hostedLargeUrl");
                    str.append(name + "\n");


                    JSONArray ingredients = recipe.getJSONArray("ingredients");

                    Vector<String> v = new Vector<String>();
                    if (ingredients != null) {
                        int len = ingredients.length();
                        for (int j=0;j<len;j++){
                            v.add(ingredients.get(j).toString());
                        }
                    }

                    int time = recipe.getInt("totalTimeInSeconds");

                    StringBuilder detail = new StringBuilder();
                    for(int j = 0; j < ingredients.length(); j++)
                        detail.append(ingredients.getString(j) + "\n");

                    vRMO.add(new RecipeMessageObject(id, "Y", name, v, sourceURL, time/60, imgURL));
                }

                if (jArray.length() == 0)
                    vRMO.add(new RecipeMessageObject("", "Y", "", new Vector<String>(), "", -2, ""));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                vRMO.add(new RecipeMessageObject("", "Y", "", new Vector<String>(), "", -2, ""));
            }


            return vRMO;


        }

        /**
         *  This function will be called when SearchAPI.execute()
         *  In this function, creating a new YummlyListActivity
         *  And pass the fields(name, ingredients) we extract from JSON to the new Activity
         *
         *  @param vRMO, RecipeMessageObject, the result returned by doInBackground()
         */
        @Override
        protected void onPostExecute(Vector<RecipeMessageObject> vRMO) {
            //Intent intent = new Intent(getApplicationContext(), YummlyListActivity.class);
            Intent intent = new Intent();
            intent.putExtra("result", vRMO);
            setResult(RESULT_OK, intent);
            finish();
            //startActivity(intent);

        }


        /**
         * Parse the web page to a single string from URL
         *
         * @param urlString, String, the URL we want to connect
         * @return String, the string contains all data of the page
         */
        private String parseURL(String urlString){
            InputStream in = null;
            String result = "";
            // HTTP Get
            try {
                URL url = new URL(urlString);

                // Get the respose from the URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                vRMO.add(new RecipeMessageObject("", "Y", "", new Vector<String>(), "", -1, ""));
                return e.getMessage();
            }

            try {
                // json is UTF-8 by default

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                // Read the return page line by line, and append all to one String
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                // Oops
                System.out.println(e.getMessage());
                vRMO.add(new RecipeMessageObject("", "Y", "", new Vector<String>(), "", -1, ""));
                return e.getMessage();

            }
            return result;

        }
    } // end SearchAPI

    /**
     * This function override the back button listener, making it no effect on press
     * because we do want to user disrupt searching
     */
    @Override
    public void onBackPressed() {
    }

}