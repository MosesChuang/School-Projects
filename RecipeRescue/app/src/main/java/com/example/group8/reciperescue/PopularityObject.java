package com.example.group8.reciperescue;

/**
 * The object used to store popularity data about a recipe
 * @author Tomas Re
 * @version 1.0
 */
public class PopularityObject {

    public String firebaseId = "";
    public int upvotes = 0;
    public int downvotes = 0;
    public boolean requestFinished = false;
    public boolean error = false;
}
