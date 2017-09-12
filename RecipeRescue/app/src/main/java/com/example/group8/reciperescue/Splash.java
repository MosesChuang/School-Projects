/**
 * Splash Class
 * This class is for displaying the splash screen.
 * <p>The splash screen has a logo at the center of the screen that rotates.
 * The animation comes from the rotate.xml (location: res/anim/).
 * The image comes from recipe.png (location: res/values/drawable/)
 *</p>
 *@author Aileen Tolentino
 *@version 1.1
 */
package com.example.group8.reciperescue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Splash extends Activity
{

    /**
     * This method displays the splash screen - the app logo that rotates.
     *
     * @param savedInstanceState, Bundle, state required to restore previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //Used for loading animation
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation ivRotate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation ivFadeo = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);


         //Starts the rotate animation and ends with the fade out animation.
        iv.startAnimation(ivRotate);
        ivRotate.setAnimationListener(new Animation.AnimationListener()
        {
            /**
             * This method notifies the start animation (rotate).
             * @param rot_animation, Animation,  creates animation for rotate
             *
             */
            @Override
            public void onAnimationStart(Animation rot_animation)
            {

            }

            /**
             * This method notifies the end animation (fade out).
             * @param fo_animation, Animation, creates animation for fade out
             */
            @Override
            public void onAnimationEnd(Animation fo_animation)
            {
                iv.startAnimation(ivFadeo);
                finish();
                Intent i = new Intent(Splash.this, MainPage.class);
                startActivity(i);
            }

            /**
             * This method notifies repetition of the animation.
             * @param rep_animation, Animation, creates animation for repeating animation
             */
            @Override
            public void onAnimationRepeat(Animation rep_animation)
            {

            }

        });
        //End of animation
    }
}
