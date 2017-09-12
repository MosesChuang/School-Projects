package com.example.group8.reciperescue;

/**
 * Created by tomas on 12/8/2015.
 */
public class SortablePopularityMessageObject implements Comparable<SortablePopularityMessageObject> {

    protected int upvotes = 0;

    protected int downvotes = 0;

    @Override
    public int compareTo(SortablePopularityMessageObject another) {
        int thisScore = this.upvotes - this.downvotes;
        int otherScore = another.upvotes - another.downvotes;

        if (otherScore > thisScore) {
            return -1;
        } else if (otherScore < thisScore) {
            return 1;
        } else {
            return 0;
        }
    }
}
