package com.example.group8.reciperescue;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.Vector;

/**
 * Provides the required tools for interacting with the popularity dataset.
 * MUST set android context
 * Firebase data is structured as: {
 * id: {
 * upvotes: 11
 * downvotes: 12
 * }
 * }
 * A recipe id is the recipeSourceAbbreviation + recipeID
 * So the popularity for example recipe Y2342323 will be stored at:
 * FIREBASE_URL/Y2342323/
 *
 * @author Tomas Re, Jeffrey Noehren
 * @version 1.0
 */
public class PopularityHelper {

    /*
    keep a reference to the carouselActivity so that we can refresh the ui when all the updates are finished
     */
    private RecipeCarousel carouselActivity;

    /*
     root Firebase database url
      */
    private String FIREBASE_URL = "https://glaring-fire-753.firebaseio.com/";

    /*
    keep a reference to the root firebase url
     */
    private Firebase rootReference = new Firebase(FIREBASE_URL);

    /*
    hold the popularity data that has been returned
     */
    private Vector<PopularityObject> popularityData;

    /*
     how many should be downloaded before it is finished
      */
    private int targetSize = 0;

    /*
    reference to the single instance of popularity helper
     */
    private static PopularityHelper popularityHelper;

    /*
    the acceptable types of Votes (can only upvote or downvote)
     */
    public static enum VoteType {
        UPVOTE, DOWNVOTE
    }

    /**
     * Registers the android context for Firebase
     * This also creates single instance of popularity helper
     * Note this is required and must be called before anything else.
     * @param context, android context about the app environment
     */
    public static void setFirebaseContext(Context context) {
        Firebase.setAndroidContext(context);
        popularityHelper = new PopularityHelper();
    }

    /**
     * Returns the single instance of the PopularityHelper class
     * @return PopularityHelper, an instance of the PopularityHelper class
     */
    public static PopularityHelper getHelper() {
        return popularityHelper;
    }

    /**
     * Registers the activity which you want to have called when all the downloads have completed
     * @param act, activity which you are trying to register against
     */
    public void registerCarouselActivity(RecipeCarousel act) {
        carouselActivity = act;
    }

    /**
     * Override the constructor so that only one instance can be created
     */
    private PopularityHelper() {

    }

    /**
     * Returns the PopularityObjects which are stored
     * @return Vector<PopularityObject>, a vector of PopularityObjects which contain the popularity data about recipes
     */
    public Vector<PopularityObject> getPopularityObjects() {
        return popularityData;
    }

    /**
     * Downloads the popularity data for the indices passed in async
     * After each popularity data has been downloaded, checks and see if it is finished downloading all of them.
     * Does this by calling checkIfDownloadsComplete() - once they are complete it calls the registeredActivity.updateRecipes
     * @param indices Vector<String>, a vector of the firebase indices to download the popularity data for
     */
    public void getRatings(Vector<String> indices) {
        popularityData = new Vector<PopularityObject>(indices.size());
        targetSize = indices.size();

        for (int i = 0; i < indices.size(); i++) {
            //System.out.println("count: " + i);
            String index = indices.elementAt(i);

            // get that object via firebase
            Firebase ref = rootReference.child(index);

            // read data set once
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    Map data;
                    int upvotes = 0;
                    int downvotes = 0;

                    // SEPARATE upvotes and downvotes for bug if one doesnt exist
                    try {
                        upvotes = (int) (long)snapshot.child("upvote").getValue();
                    } catch (NullPointerException e) {
                        // will get thrown if data is null - no data for the object
                        upvotes = 0;
                    }
                    try {
                        downvotes = (int) (long)snapshot.child("downvote").getValue();
                        //System.out.println(snapshot.getKey() + ": upvotes:" + upvotes + ":downvotes:" );

                    } catch (NullPointerException e) {
                        // will get thrown if data is null - no data for the object
                        //System.out.println("PopularityHelper.getRatings exception");
                        //System.out.print(e.getMessage());
                        downvotes = 0;
                    }

                    PopularityObject popularityObject = new PopularityObject();
                    popularityObject.firebaseId = snapshot.getKey();
                    popularityObject.upvotes = upvotes;
                    popularityObject.downvotes = downvotes;
                    popularityObject.error = false;
                    popularityObject.requestFinished = true;
                    popularityData.addElement(popularityObject);

                    //System.out.println("PopularityHelper.getRatings snapshot done");

                    checkIfDownloadsCompleted();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("PopularityHelper.getRatings addListenerForSingleValueEvent cancelled");
                    System.out.println(firebaseError.getMessage());
                    PopularityObject popularityObject = new PopularityObject();
                    popularityObject.requestFinished = true;
                    popularityObject.error = true;
                    popularityData.addElement(popularityObject);

                    checkIfDownloadsCompleted();
                }
            });
        }
    }

    /**
     * updates the recipes list for the registered activity if all recipes have been downloaded
     */
    private void checkIfDownloadsCompleted() {
        if (popularityData.size() >= targetSize) {
            carouselActivity.updateRecipes(popularityData);
        }
    }

    /**
     * saves the vote to firebase
     * @param type VoteType, the type of vote event which occured, upvote or downvote
     * @param firebaseId String, the firebase id which with the vote should be registered for
     */
    public void pushVoteToDatabase(VoteType type, String firebaseId) {
        // field name to pass to firebase
        String fieldName;

        if (type == VoteType.UPVOTE) {
            fieldName = "upvote";
        } else if (type == VoteType.DOWNVOTE) {
            fieldName = "downvote";
        } else {
            // default to upvote
            fieldName = "upvote";
        }

        final Firebase ref = rootReference.child(firebaseId + "/" + fieldName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                long value;
                try {
                    // will throw an exception if null - no value ever stored
                    value = (long) snapshot.getValue();
                } catch (NullPointerException e) {
                    value = 0;
                }

                // increase value by 1
                value += 1;

                ref.setValue(value);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("PopularityHelper.pushVoteToDatabase addListenerForSingleValueEvent cancelled");
                System.out.println(firebaseError.getMessage());
            }
        });
    }


    /**
     * Injects all the popularity data (upvotes / downvotes) into recipe message objects
     * @param recipeMessageObjects Vector<RecipeMessageObject>, an array of recipe message objects which gets the popularity data injected in
     * @param popularityObjects Vector<PopularityObject>, an array of popularity objects which contains the data injected into recipe message objects
     */
    public static void injectPopularityIntoRecipeMessageObjects(Vector<RecipeMessageObject> recipeMessageObjects, Vector<PopularityObject> popularityObjects) {
        // go through and inject the popularity fields into the recipe message object
        for (int i = 0; i < recipeMessageObjects.size(); i++) {

            RecipeMessageObject recipeObject = recipeMessageObjects.elementAt(i);
            String id = computeFirebaseId(recipeObject);

            /*
            popularityObjects not guaranteed to be in order
            loop through and match to the id
            if there is no object or an error - dont care will default to RANDOM TODO - ALSO put firebase error on RCP object to not update from error
            */
            for (int j = 0; j < popularityObjects.size(); j++) {
                if (popularityObjects.elementAt(j).firebaseId.equals(id) && !popularityObjects.elementAt(j).error) {
                    recipeObject.setUpvotes(popularityObjects.elementAt(j).upvotes);
                    recipeObject.setDownvotes(popularityObjects.elementAt(j).downvotes);
                    System.out.println("MATCHED: " + PopularityHelper.computeFirebaseId(recipeObject) + " upvotes: " + recipeObject.getUpvotes());
                }
            }
        }
    }

    /**
     * gets a list of all the firebase ids from a list of recipe message objects
     * @param recipeMessageObjects Vector<RecipeMessageObject>, an array of recipe message objects to get firebase ids for
     * @return Vector<String>, an array of strings containing all the firebase ids
     */
    public static Vector<String> getListOfFirebaseIds(Vector<RecipeMessageObject> recipeMessageObjects) {

        // vector array to be returned
        Vector<String> firebaseIds = new Vector<String>(recipeMessageObjects.size());

        for (int i = 0; i < recipeMessageObjects.size(); i++) {
            // TODO guaranteed to be non empty?
            firebaseIds.insertElementAt(computeFirebaseId(recipeMessageObjects.elementAt(i)), i);
        }

        return firebaseIds;
    }

    /**
     * returns the firebase id for the passed in recipe message object
     * @param recipeMessageObject RecipeMessageObject, a recipe message object to get the firebase id for
     * @return String, the firebase id for the passed in recipe message object
     */
    public static String computeFirebaseId(RecipeMessageObject recipeMessageObject) {
        return recipeMessageObject.recipeSourceAbbreviation + recipeMessageObject.recipeID;
    }
}
