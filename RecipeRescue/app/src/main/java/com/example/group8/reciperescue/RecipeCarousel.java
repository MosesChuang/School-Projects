/**
 *RecipeCarousel Class
 * This class is for retrieving recipes and displaying the recipe image in a carousel. The user can
 * swipe the images from left to rigth or right to left.
 *
 * @author Aileen Tolentino, Tomas Re, Moses Chuang, Jeffrey Noehren
 */
package com.example.group8.reciperescue;

import java.io.InputStream;
import java.net.URL;

import android.os.AsyncTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.Point;
import android.text.util.Linkify;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.TreeMap;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class RecipeCarousel extends Activity {

    public final int UPVOTE_NOT_PRESSED = R.drawable.upvote;
    public final int UPVOTE_PRESSED = R.drawable.upvote_pressed;
    public final int DOWNVOTE_NOT_PRESSED = R.drawable.downvote;
    public final int DOWNVOTE_PRESSED = R.drawable.downvote_pressed;

    // the actual recipe message objects

    ImageView img;
    Bitmap bitmap;
    TreeMap <Integer, Bitmap> decoded = new TreeMap<>();
    Vector<RecipeMessageObject> recipes;

    //Recipe image from the url in recipe object
    ImageView iv;

    // which page we are on
    private int pageIndex = 0;

    // string to search the api with
    private String apiQueryString;

    // get comma separated string
    private void getSelectedIngredients() {
        Vector<String> ingredients = IngredientSelection.selectedIngredients;

        String queryString = "";

        if (ingredients.size() < 1) {
            queryString = "Cookie";
        }

        for (int i = 0; i < ingredients.size(); i++) {
            queryString += ingredients.elementAt(i);

            if (i < ingredients.size() - 1) {
                queryString += ',';
            }
        }

        apiQueryString = queryString;
    }

    /**
     * This method updates the recipe search results.
     * @param popularityObjects
     */
    public void updateRecipes(Vector<PopularityObject> popularityObjects) {
        PopularityHelper.injectPopularityIntoRecipeMessageObjects(recipes, popularityObjects);
        List<SortablePopularityMessageObject> r = (List)recipes;
        Collections.sort(r, Collections.reverseOrder());

        // LOAD IMAGES AFTER RECIPES HAVE BEEN SORTED
        for(int i = 0; i < recipes.size(); i++)
            new LoadImage().execute(i);

        refreshUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_carousel);
        decoded.clear();
        ImageAdapter.decoded.clear();
        for(int i = 0; i < 10; i++)
            ImageAdapter.decoded.put(i, BitmapFactory.decodeResource(RecipeCarousel.this.getResources(),
                    R.drawable.default_icon));

        recipes = new Vector<RecipeMessageObject>();

        getSelectedIngredients();

        System.out.println(apiQueryString);

        Intent Intent = new Intent(getApplicationContext(), RecipeAPIActivity.class);
        Intent.putExtra("query", apiQueryString);
        startActivityForResult(Intent, 3);
    }

    /**
     *This method collects the data from the RecipeMessageObject and use it.
     *
     * @param requestCode, int, the ID of each request
     * @param resultCode, int, the ID of each  result
     * @param data, Intent, the intent contains extra messages
     */
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        // Collect data from the intent and use it
        Serializable value = data.getSerializableExtra("resultAPI");
        List<RecipeMessageObject> lRMO = (List<RecipeMessageObject>) value;
        if(lRMO!=null) {
            for (int i = 0; i < lRMO.size(); i++) {
                recipes.add(lRMO.get(i));
            }
        }
        String mY = data.getStringExtra("mY");
        String mS = data.getStringExtra("mS");

        if (!mY.equals("OK") && !mS.equals("OK")) {
        //if (lRMO.size() == 1) {
            CharSequence text;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
/*
            if (lRMO.get(0).timeRequired == -1) {
                text = "no internet connection!";
            } else {
                text = "Sorry we can't find any recipe!";
            }
*/
            text = mY;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent intent2 = new Intent();
            intent2 = new Intent(this, MainPage.class);
            startActivity(intent2);
            finish();
        } else {
            PopularityHelper helper = PopularityHelper.getHelper();

            helper.registerCarouselActivity(this);
            Vector <String> firebaseIds = PopularityHelper.getListOfFirebaseIds(recipes);

            helper.getRatings(firebaseIds);


            //For implementing carousel
            ImageAdapter.size = recipes.size();
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            ImageAdapter adapter = new ImageAdapter(this);
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
            {
                private int currentPage;

                @Override
                public void onPageSelected(int pos)
                {
                    if (pos < recipes.size())
                        pageIndex = pos;
                    else
                        pageIndex = recipes.size() - 1;
                    System.out.println(pos);

                    refreshUI();
                }

                public final int getCurrentPage()
                {
                    return pageIndex;
                }
            });


            //refreshUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_carousel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method updates all the text views and such with the information for the current
     * index of out ingredients list.
     */
    public void refreshUI() {
        // contains all the properties of the object we want to display
        RecipeMessageObject currentObject = recipes.elementAt(pageIndex);

        //Used to set customized font. Font located in he assests folder.
        Typeface appCustomFont = Typeface.createFromAsset(getAssets(), "fonts/abel.ttf");

        //Display recipe title
        TextView titleText = (TextView)findViewById(R.id.actualtitle);
        titleText.setText(currentObject.title + "\n\n");
        titleText.setTypeface(appCustomFont, Typeface.BOLD);

        //Display cooking time
        TextView timeText = (TextView)findViewById(R.id.time_actual);
        timeText.setText("" + currentObject.timeRequired + " minutes" + "\n\n");
        timeText.setTypeface(appCustomFont);

        //Display recipe link
        TextView recipeLinkText = (TextView)findViewById(R.id.recipelinkactual);
        recipeLinkText.setText(currentObject.recipeURL);
        recipeLinkText.setTypeface(appCustomFont);

        Linkify.addLinks(recipeLinkText, Linkify.ALL);

        //Display the ingredients
        TextView ingredientsText = (TextView)findViewById(R.id.ingredientsactual);
        Vector<String> ingredients = currentObject.ingredients;
        String ingredientsString = "";
        for (int i = 0; i < ingredients.size(); i++)
        {
            ingredientsString += ingredients.elementAt(i) + ",";
        }
        ingredientsText.setText(ingredientsString);
        ingredientsText.setTypeface(appCustomFont, Typeface.BOLD);

        // display score
        TextView scoreText = (TextView) findViewById(R.id.scoreField);
        scoreText.setText("score:" + (currentObject.upvotes - currentObject.downvotes));
        scoreText.setTypeface(appCustomFont);

        setUpvoteDownvoteButtonResources(currentObject);

    }

    public void mapsButtonClickHandler(View view){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    /**
     * This method goes to the previous recipe when the previous button is clicked
     * @param view, View, the previous button
     */
    public void previousButtonClickHandler(View view)
    {
        int length = recipes.size();
        // TODO recipes must be > 0 or crash
        if (pageIndex == 0) {
            pageIndex = length - 1;
        } else {
            pageIndex--;
        }

        refreshUI();
    }

    /**
     *This method goes to the next recipe when the previous button is clicked
     * @param view, View, the next button
     */

    public void nextButtonClickHandler(View view)
    {
        int length = recipes.size();
        // we are already at the last one
        if (pageIndex >= length - 1) {
            // loop around
            pageIndex = 0;
        } else {
            // next view
            pageIndex++;
        }

        refreshUI();
    }

    public void downvoteClickHandler(View view)
    {
        RecipeMessageObject currentObject = recipes.elementAt(pageIndex);

        if (currentObject.downvotePressed || currentObject.upvotePressed) {
            // already been pressed exit
            return;
        }
        // downvote or upvote button not already pressed
        // set it to has already been pressed
        currentObject.downvotePressed = true;
        // update the score
        currentObject.downvotes++;
        // update the ui
        refreshUI();

        String id = PopularityHelper.computeFirebaseId(currentObject);
        PopularityHelper.getHelper().pushVoteToDatabase(PopularityHelper.VoteType.DOWNVOTE, id);
    }

    public void upvoteClickHandler(View view)
    {
        RecipeMessageObject currentObject = recipes.elementAt(pageIndex);

        if (currentObject.downvotePressed || currentObject.upvotePressed) {
            // already been pressed exit
            return;
        }
        // downvote or upvote button not already pressed
        // set it to has already been pressed
        currentObject.upvotePressed = true;
        // update the score
        currentObject.upvotes++;
        // update the ui
        refreshUI();

        String id = PopularityHelper.computeFirebaseId(currentObject);
        PopularityHelper.getHelper().pushVoteToDatabase(PopularityHelper.VoteType.UPVOTE, id);
    }


    /**
     *  This is an AsyncTask to load recipe image from URL
     *  When it is executed, it will call doInBackground(), then call onPostExcute()
     *
     *  @author Moses Chuang
     */
    private class LoadImage extends AsyncTask<Integer, String, Bitmap>
    {
        /**
         *  This function will be called when LoadImage.execute()
         *  In this function, decoded image from the URL of the current recipe,
         *  resize the bitmap to fixed size and put it to ImageAdapter,
         *  and catch the exception if error occurs while retrieving the image
         *
         *  @param args, Integer..., The offset of the current recipe
         *  @return Bitmap, the decoded image from the URL of the current recipe
         */
        protected Bitmap doInBackground(Integer... args) {
            try {
                String url = recipes.get(args[0]).imgURL;
                //if(ImageAdapter.decoded.containsKey(recipes.get(args[0]).imgURL)) {
                //    bitmap = ImageAdapter.decoded.get(args[0]);
                //} else {
                Point DeviceSize = new Point();
                getWindowManager().getDefaultDisplay()
                        .getSize(DeviceSize);
                System.out.println(DeviceSize.x);
                System.out.println(DeviceSize.y);
                    Bitmap bitmap0;
                    bitmap0 = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                    bitmap = Bitmap.createScaledBitmap(bitmap0, 900,
                            600, false);
                    ImageAdapter.decoded.put(args[0], bitmap);
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         *  This function will be called when LoadImage.execute()
         *  In this function, using the URL to get information from Yummlu Database,
         *  And parse the returned JSON format, extract the field we want.
         *
         *  @param image, Bitmap, The image of the current recipe
         */
        protected void onPostExecute(Bitmap image) {

            if(image != null){
                //iv.setImageBitmap(image);
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPager.getAdapter().notifyDataSetChanged();

            }else{
                Toast.makeText(RecipeCarousel.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    // NOTE upvote downvote pressed parameters must be set prior to calling this method
    private void setUpvoteDownvoteButtonResources(RecipeMessageObject recipeObj)
    {

        // get a reference to the buttons
        ImageView upvoteButton = (ImageView)findViewById(R.id.upvoteButton);
        ImageView downvoteButton = (ImageView)findViewById(R.id.downvoteButton);
        if (recipeObj.downvotePressed) {
            downvoteButton.setImageResource(DOWNVOTE_PRESSED);
        } else {
            downvoteButton.setImageResource(DOWNVOTE_NOT_PRESSED);
        }

        if (recipeObj.upvotePressed) {
            upvoteButton.setImageResource(UPVOTE_PRESSED);
        } else {
            upvoteButton.setImageResource(UPVOTE_NOT_PRESSED);
        }


    }

}
