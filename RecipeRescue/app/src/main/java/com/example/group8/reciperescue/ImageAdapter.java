
package com.example.group8.reciperescue;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.graphics.Bitmap;

import java.util.TreeMap;

/**
 * ImageAdapter Class
 * This class is used for implementing the recipe image carousel. It populates the carousel with the
 * images from the recipe search results. The user can swipe the recipe images from left to right
 * and righ to left.
 *
 * @author Aileen Tolentino, Moses Chuang
 */
public class ImageAdapter extends PagerAdapter
{
    Context context;

    static public TreeMap<Integer, Bitmap> decoded = new TreeMap<>();
    static public int size = 10;

    ImageAdapter(Context context)
    {
        this.context=context;
    }

    /**
     * This method returns the number of recipe image results.
     * @return size, int, the number of recipe images from search results
     */
    @Override
    public int getCount()
    {
        return size;
    }

    /**
     * This method determines whether a page View is associated with a specific key object as
     * returned by instantiateItem.
     * @param view View, page, view to check for association with object
     * @param object, Object, object to check for association with view
     * @return view, View,  the recipe image
     */
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((ImageView) object);
    }

    /**
     * This method is called when the host view is attempting to determine if an image's position
     * has changed.
     * @param object, Object, object representing the image
     * @return POSITION_NONE, int, position of the object not present
     */
    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    /**
     * This method creates the page for the image with the given position. The adapter is
     * responsible for adding the view to container.
     * @param container, ViewGroup, the containing View in which the page will be shown
     * @param position, int, the page position to be instantiated
     * @return imageView, ImageView, returns the new image based on its position.
     */
      @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(context);
        //int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);

        imageView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(ImageAdapter.decoded.get(position));
        //imageView.setImageResource(GalImages[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    /**
     * This method removes the page in the container from the given position.
     *
     * @param container, ViewGroup, the containing View from which the page will be removed
     * @param position, int, the page position to be removed
     * @param object, Object, the object that was returned by instantiateItem
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
