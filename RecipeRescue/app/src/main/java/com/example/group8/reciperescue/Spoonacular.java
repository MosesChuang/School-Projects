package com.example.group8.reciperescue;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Main activity of Spoonacular API.
 * Search and get recipe information from the ingredient list the user selected
 * <p>
 * Recipes don't contain cooking instructions
 * @author Jia Chen
 * @version 1.1
 */
public class Spoonacular extends Activity {
    Vector<RecipeMessageObject> SpoonacularOBJ;
    /**
     * Start activity
     * @param savedInstanceState, Bundle, save state before pause the program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String dummy = getIntent().getStringExtra("q");
        new CallAPI().execute(dummy);
    }

    /*@Override
    protected void onStart() {
        //super.onStart();
        //setVisible(true);
    }*/


    /**
     * Call Spoonacular API in background
     * <p>
     * @author Jia Chen
     * @version 1.1
     */
    private class CallAPI extends AsyncTask<String, String, Vector<RecipeMessageObject>> {
        /**
         * Connect to Spoonacular API and search with the ingredients the user selected.
         * @param params, String, Ingredients the user entered
         * @return Vector<RecipeMessageObject> , a list of 5 Spoonacualr objects
         */
        @Override
        protected Vector<RecipeMessageObject> doInBackground(String... params) {
            SpoonacularOBJ = new Vector<RecipeMessageObject>();
            String ingr = params[0];
            String recipeInfo="";
            int totalIngr[];
            int i, j;
            int resultCount = 5;    //number of return results
            String url1 = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" +
                    "findByIngredients?ingredients=" + ingr +
                    "&number=" + resultCount;

            totalIngr = new int[resultCount];

            for(i = 0; i < resultCount; i++){
                totalIngr[i] = 0;
            }

            //Search recipes
            try {
                URL url = new URL(url1);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("X-Mashape-Key","Js8CHgO9NFmsh9DNY1g5kSfEm9zdp1yzwPSjsnlIGp7swegABB");
                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                recipeInfo = reader.readLine();
            } catch (Exception ex) {
                ex.printStackTrace();
                Vector<String> ingredient = null;
                RecipeMessageObject ro = new RecipeMessageObject("", "", "", ingredient, "", -1,"");
                SpoonacularOBJ.add(ro);
                return  SpoonacularOBJ;
            }

            try {
                JSONArray recipes = new JSONArray(recipeInfo);
                String recipeID;
                String recipeSourceAbbreviation;
                String title;
                String recipeURL;
                int timeRequired;
                String image;


                for (i = 0; i < resultCount; i++) {
                    Vector<String> ingredient = new Vector<String>();

                    JSONObject temp = recipes.getJSONObject(i);
                    title = temp.getString("title");
                    recipeID = Integer.toString(temp.getInt("id"));
                    recipeSourceAbbreviation = "S";
                    totalIngr[i] += temp.getInt("usedIngredientCount");
                    totalIngr[i] += temp.getInt("missedIngredientCount");

                    //Get recipe information
                    try {
                        String getURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" +
                                recipeID +
                                "/information";
                        URL url = new URL(getURL);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("X-Mashape-Key", "Js8CHgO9NFmsh9DNY1g5kSfEm9zdp1yzwPSjsnlIGp7swegABB");
                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(isr);
                        recipeInfo = reader.readLine();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    temp = new JSONObject(recipeInfo);
                    timeRequired = temp.getInt("readyInMinutes");
                    recipeURL = temp.getString("sourceUrl");
                    JSONArray ingredientAry = temp.getJSONArray("extendedIngredients");
                    image = temp.getString("image");

                    //get ingredient list
                    for(j = 0; j < totalIngr[i]; j++) {
                        ingredient.add(ingredientAry.getJSONObject(j).getString("name"));
                    }

                    SpoonacularOBJ.add(new RecipeMessageObject(recipeID, recipeSourceAbbreviation, title, ingredient, recipeURL, timeRequired, "https://spoonacular.com/recipeImages/" + image));
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
                Vector<String> ingredient = null;
                RecipeMessageObject ro = new RecipeMessageObject("-2", "", "", ingredient, "", -2, "");
                SpoonacularOBJ.add(ro);
                return  SpoonacularOBJ;
            }
            return SpoonacularOBJ;
        } // end doInBackground

        /**
         * Send the result to merge in RecipeAPIActivity activity
         * @param result, Vector<RecipeMessageObject>, list of 5 RecipeMessageObjects
         * @return void, not return anything
         */
        protected void onPostExecute(Vector<RecipeMessageObject> result) {

            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(RESULT_OK, intent);
            finish();
        }
    } // end CallAPI

    /**
     * This function override the back button listener, making it no effect on press
     * because we do want to user disrupt searching
     */
    @Override
    public void onBackPressed() {
    }
} // end Spoonacular