package com.example.group8.reciperescue;

/**
 * Created by kfang on 11/24/2015.
 */
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Wraps the MapFragment inside a custom View in order to produce our own custom view.
 * @author Kevin Fang
 * @version 1.1
 */

public class MapWrapperLayout extends RelativeLayout {
    /**
     * Reference to a GoogleMap object
     */
    private GoogleMap map;

    /**
     * Vertical distance in pixels between the bottom edge of our InfoWindow
     * and the marker position (by default it's bottom edge too).
     */
    private int bottomOffsetPixels;

    /**
     * A currently selected marker
     */
    private Marker marker;

    /**
     * Our custom view which is returned from either the InfoWindowAdapter.getInfoContents
     * or InfoWindowAdapter.getInfoWindow
     */
    private View infoWindow;

    /**
     * The view of the MapWrapperLayout
     * @param context, Context, The view in which you are working with is associated with context
     */
    public MapWrapperLayout(Context context) {
        super(context);
    }

    /**
     * The view of the MapWrapperLayout with specific attributes
     * @param context, Context, The view in which you are working with
     * @param attrs, AttributeSet, The attributes that relate to the MapWrapperLayout
     */
    public MapWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * The view of the MapWrapperLayout with specific attributes and a specific style
     * @param context, Context, The view in which you are working with
     * @param attrs, The attributes that relate to the MapWrapperLayout
     * @param defStyle, int, the style chosen
     */
    public MapWrapperLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Has a map so that it can route the touch event
     * @param map, GoogleMap, the map we passed
     * @param bottomOffsetPixels, int, the pixel size of the bottom of the view
     */
    public void init(GoogleMap map, int bottomOffsetPixels) {
        this.map = map;
        this.bottomOffsetPixels = bottomOffsetPixels;
    }

    /**
     * Sets specific marker with the infowindow
     * @param marker, Marker, the marker for the infoWindow
     * @param infoWindow, View, the infoWindow we are currently viewing
     */
    public void setMarkerWithInfoWindow(Marker marker, View infoWindow) {
        this.marker = marker;
        this.infoWindow = infoWindow;
    }

    /**
     * Triggers when event is called in infoWindow
     * @param ev, MotionEvent, The MotionEvent object containing full information about the event.
     * @return boolean, true if infoWindow fills touch event else pass event and return result
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = false;
        // Make sure that the infoWindow is shown and we have all the needed references
        if (marker != null && marker.isInfoWindowShown() && map != null && infoWindow != null) {
            // Get a marker position on the screen
            Point point = map.getProjection().toScreenLocation(marker.getPosition());

            // Make a copy of the MotionEvent and adjust it's location
            // so it is relative to the infoWindow left top corner
            MotionEvent copyEv = MotionEvent.obtain(ev);
            copyEv.offsetLocation(
                    -point.x + (infoWindow.getWidth() / 2),
                    -point.y + infoWindow.getHeight() + bottomOffsetPixels);

            // Dispatch the adjusted MotionEvent to the infoWindow
            ret = infoWindow.dispatchTouchEvent(copyEv);
        }

        // If the infoWindow consumed the touch event, then just return true.
        // Otherwise pass this event to the super class and return it's result
        return ret || super.dispatchTouchEvent(ev);
    }
}
