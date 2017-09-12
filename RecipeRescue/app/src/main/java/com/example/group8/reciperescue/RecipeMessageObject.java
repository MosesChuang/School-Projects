package com.example.group8.reciperescue;

import java.io.Serializable;
import java.util.Vector;

/**
 * Class for storing recipe information required.
 * @author Jeffrey Noehren, Tomas Re
 * @date 10/24/2015
 * @version 1.1
 */
public class RecipeMessageObject extends SortablePopularityMessageObject implements Serializable {
    // the title of the recipe
    public String title;

    // the list of the ingredients for this recipe
    public Vector<String> ingredients;

    // the URL to the full recipe
    public String recipeURL;

    // the URL to the image
    public String imgURL;


    // time required to make the food in minutes, either cook time or prep time for now
    public int timeRequired;

    // now inherited for sorting
    // how many upvotes this recipe has recieved so far
    //public int upvotes = 0;

    // now inherited
    // how many downvotes this recipe has recieved so far
    //public int downvotes = 0;

    // keep track of whether they have downvoted a recipe
    public boolean downvotePressed = false;

    // keep track of whether they have upvoted a recipe
    public boolean upvotePressed = false;

    // the ID of the recipe (from yummly or spoontacular)
    public String recipeID;

    // the source, NOTE: must be 'Y' or 'S'
    public String recipeSourceAbbreviation;

    /**
     * Constructor to create the recipe message objects with all the required parameters.
     * @param recipeID String, the id associated with specific recipe.
     * @param recipeSourceAbbreviation String, the abbreviation of the source, either "S" or "Y".
     * @param title String, the title of the recipe.
     * @param ingredients Vector<String>, array of strings of the ingredients for the recipe.
     * @param recipeURL String, the url of the source recipe with instructions.
     * @param timeRequired int, the time required to make the food in minutes.
     */
    public RecipeMessageObject(String recipeID, String recipeSourceAbbreviation, String title, Vector<String> ingredients, String recipeURL, int timeRequired, String imgURL) {
        this.recipeID = recipeID;
        this.recipeSourceAbbreviation = recipeSourceAbbreviation;
        this.title = title;
        this.ingredients = ingredients;
        this.recipeURL = recipeURL;
        this.timeRequired = timeRequired;
        this.imgURL = imgURL;

    }

    public int getDownvotes () {
        return super.downvotes;
    }

    public void setDownvotes(int d) {
        super.downvotes = d;
    }

    public int getUpvotes () {
        return super.upvotes;
    }

    public void setUpvotes(int d) {
        super.upvotes = d;
    }



}
