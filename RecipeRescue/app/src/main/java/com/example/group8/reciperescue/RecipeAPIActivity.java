package com.example.group8.reciperescue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 *  Created by Moses Chaung.
 *
 *  This is the  activity of Combining 2 recipe API.
 *  This activity starts Yummly and Spoonaculer Intents, passing query string and get result.
 *
 * @author Moses Chuang, Jia Chen
 */
public class RecipeAPIActivity extends Activity {
    /**
     *  The Vector storing the RecipeMessageObject List
     */
    Vector<RecipeMessageObject> vRMO;
    String error = "OK";
    String mS = "";
    String mY = "";


    /**
     * Initializes the components for the Common Recipe API actions.
     * @param savedInstanceState Bundle object, state required to restore previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        vRMO = new Vector<RecipeMessageObject>();
        String query = getIntent().getStringExtra("query");



        //if no ingredient entered, jump back to main page
        if(query.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Enter ingredient!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
        }
        else {
            query = query.replace(" ", "%20");

            Intent yummlyIntent = new Intent(getApplicationContext(), YummlyActivity.class);
            yummlyIntent.putExtra("q", query);

            startActivityForResult(yummlyIntent, 1);

            Intent spoonIntent = new Intent(getApplicationContext(), Spoonacular.class);
            spoonIntent.putExtra("q", query);

            startActivityForResult(spoonIntent, 2);

        }

    }

    /**
     *
     * Get the return data from Yummly and Spoonacular,
     * store the data into a vector of RecipeMessageObject,
     * and return the vector to RecipeCarousel activity
     *
     * @param requestCode, int, the ID of each request
     * @param resultCode, int, the ID of each  result
     * @param data, Intent, the intent contains extra messages
     */
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        Serializable value = data.getSerializableExtra("result");
        List<RecipeMessageObject> lRMO = (List<RecipeMessageObject>) value;

        if(lRMO.get(0).timeRequired == -1) {
            error = "Please check your connection and search again!";
            lRMO.clear();
        } else if(lRMO.get(0).timeRequired == -2){
            error = "Sorry we can't find any recipes!";
            lRMO.clear();
        } else
            error = "OK";

        for (int i = 0; i < lRMO.size(); i++) {
            vRMO.add(lRMO.get(i));
            System.out.println(vRMO.size());
        }

        if (requestCode == 1)
            mY = error;


        if (requestCode == 2) {
            mS = error;

                Intent intent = new Intent();
                intent.putExtra("resultAPI", vRMO);
                intent.putExtra("mY", mY);
                intent.putExtra("mS", mS);


            setResult(RESULT_OK, intent);
                finish();
        }
    }

    /**
     * This function override the back button listener, making it no effect on press
     * because we do want to user disrupt searching
     */
    @Override
    public void onBackPressed() {
    }


}