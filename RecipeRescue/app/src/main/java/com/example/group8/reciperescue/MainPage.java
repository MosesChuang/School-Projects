/**
 * Mainpage Class
 * This class is the main page of the app.
 *
 * Before showing the mainpage of the app, this class checks the status of the location service. If
 * it is enabled, the EULA screen is displayed. If it is disabled, it prompts the user to enable the
 * location service.
 *
 * After the location service is checked and enabled, the EULA screen will be displayed. The user
 * can accept it or cancel it. If the user cancels it, the app will close.
 *
 * @author Aileen Tolentino & Jeffrey Noehren
 */
package com.example.group8.reciperescue;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


public class MainPage extends Activity
{
    /**
     * When the program starts we set the content to be the split screen view with the food button
     * on the left and drinks button on the right. Then the EULA is presented if it has not been
     * agreed upon before.
     * @param savedInstanceState Bundle
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //Show EULA to user
        new Eula(this).show();

        //Start of checking location services
        final Context context;
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        boolean locStat;

        if (locationManager == null)

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }

        try
        {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }

        locStat = gps_enabled || network_enabled;

        //Dialog box for enabling location services
        if (!locStat)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Enable Location Services");
            dialog.setMessage("This app requires that your location services be enabled for certain features to work." +
                    "Do You want to enable your location services?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int enable)
                {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }

            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });

            dialog.show();
        }// END of location services

        // set the context for Firebase to work
        PopularityHelper.setFirebaseContext(getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    /**
     * This method redirects the user to the food ingredients screen upon touching the
     * food button
     * @param view, View, the food button
     */
    public void onClickFood(View view)
    {
        Intent i=new Intent(MainPage.this, FoodIngredientSelection.class);
        startActivity(i);
    }

    /**
     * This method redirects the user to the beverage ingredients screen upon touching the
     * beverage button
     * @param view, View, the beverage button
     */
    public void onClickBeverage(View view)
    {
        Intent i=new Intent(MainPage.this, DrinkIngredientSelection.class);
        startActivity(i);
    }

    /**
     * This methods shows the RecipeMessageOject from the recipe API
     * @param view, View, the search button
     */
    public void searchButtonHandler(View view)
    {
        Intent i=new Intent(MainPage.this, RecipeCarousel.class);
        startActivity(i);
    }

}