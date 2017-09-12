package com.example.group8.reciperescue;

/**
 * Created by kfang on 11/24/2015.
 */
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.gms.maps.model.Marker;


/**
 * Actions that occur when the events in the infoWindow are triggered.
 * @author Kevin Fang
 * @see <a href="http://stackoverflow.com/questions/14123243/google-maps-android-api-v2-interactive-infowindow-like-in-original-android-go/15040761#15040761"</a>
 * @version 1.1
 */

public abstract class OnInfoWindowElemTouchListener implements OnTouchListener {
    private final View view;
    private final Drawable bgDrawableNormal;
    private final Drawable bgDrawablePressed;
    private final Handler handler = new Handler();
    private Marker marker;
    private boolean pressed = false;

    /**
     * constructor for the infoWindowTouchListener
     * @param view, View, the view of the infoWindow
     * @param bgDrawableNormal, Drawable, State of the button when not pressed
     * @param bgDrawablePressed, Drawable, State of the button when pressed
     */
    public OnInfoWindowElemTouchListener(View view, Drawable bgDrawableNormal, Drawable bgDrawablePressed) {
        this.view = view;
        this.bgDrawableNormal = bgDrawableNormal;
        this.bgDrawablePressed = bgDrawablePressed;
    }

    /**
     * Sets the marker
     * @param marker, Marker, the marker that will be set
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     * What happens on touch of the events
     * @param vv, View, The view of the event touched
     * @param event, MotionEvent, The MotionEvent object containing full information about the event.
     * @return boolean, if pressed show a pressed state other wise return false
     */
    @Override
    public boolean onTouch(View vv, MotionEvent event) {
        if (0 <= event.getX() && event.getX() <= view.getWidth() &&
                0 <= event.getY() && event.getY() <= view.getHeight())
        {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: startPress(); break;

                // We need to delay releasing of the view a little so it shows the pressed state on the screen
                case MotionEvent.ACTION_UP: handler.postDelayed(confirmClickRunnable, 150); break;

                case MotionEvent.ACTION_CANCEL: endPress(); break;
                default: break;
            }
        }
        else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress();
        }
        return false;
    }

    /**
     * If the state is pressed show a pressed state and if the marker has an info window show it
     */
    private void startPress() {
        if (!pressed) {
            pressed = true;
            handler.removeCallbacks(confirmClickRunnable);
            view.setBackground(bgDrawablePressed);
            if (marker != null)
                marker.showInfoWindow();
        }
    }

    /**
     * When the press is released show a different background and that it is no longer pressed
     * @return boolean, returns true if pressed else returns false
     */
    private boolean endPress() {
        if (pressed) {
            this.pressed = false;
            handler.removeCallbacks(confirmClickRunnable);
            view.setBackground(bgDrawableNormal);
            if (marker != null)
                marker.showInfoWindow();
            return true;
        }
        else
            return false;
    }

    /**
     * Makes a new Runnable that is confirmClickRunnable
     */
    private final Runnable confirmClickRunnable = new Runnable() {
        /**
         * If the state is in endpress run onClickConfirmed
         */
        public void run() {
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };

    /**
     * This is called after a successful click
     * @param v, View, The view of the marker
     * @param marker, Marker, the Specific marker we are dealing with
     */
    protected abstract void onClickConfirmed(View v, Marker marker);
}
