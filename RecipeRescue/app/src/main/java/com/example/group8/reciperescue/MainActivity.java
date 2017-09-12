
/**
 * MainActivity Class
 * This class launches the app and then displays the splash and EULA.
 *
 * @author Aileen Tolentino
 * @version 1.1
 */

package com.example.group8.reciperescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
{
    /**
     * This method does the following on app launch:
     *   1. displays the splash screen and
     *   2. displays the EULA.
     *
     * @param Bundle, savedInstanceState, state required to restore previous state
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
        @tomas commented out setContentView
        referencing R.layout.activity_main which no longer exists - was just the generated file
         */

        //Shows EULA after splash screen - Moved to MainPage.java
        //new Eula(this).show();
    }
    

    /**
     * This method initializes the contents of the app's standard options menu. It adds items
     * on the menu.
     *
     * @param Menu, menu, adds menu item to the action bar
     * @return boolean, return true if there are items to be added on the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method handles any selected option from the menu.
     * @param item, MenuItem, menu option selected by the user
     * @return boolean, return true if any menu option is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
