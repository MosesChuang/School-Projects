/**
 * EULA Class
 * This class is for displaying the EULA before the user can start using the app.
 *
 * On app launch, the user is prompted with the EULA screen.
 *
 * If the user selects "Ok", the user will
 * be redirected to the app's main screen. Once EULA is accepted by the user, the EULA screen will
 * not be displayed again, except if there are any updates.
 *
 * If the user selects "Cancel", the app will close and the EULA will be displayed again.
 *
 * @author Aileen Tolentino
 * @see https://jayeshcp.wordpress.com/2013/07/23/showing-eula-in-android-app/
 * @version 1.1
 */
package com.example.group8.reciperescue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;



public class Eula
{

    private String EULA_PREFIX = "eula_";
    private Activity mActivity;

    /**
     * This method creates a new activity to get the app information.
     * @param context, Activity, creates a new activity for EULA
     */
    public Eula(Activity context)
    {
        mActivity = context;
    }

    /**
     * This method retrieves overall information about an application package. It collects the app
     * information from the manifest file.
     *
     * @return AppInfo, PackageInfo, returns the application information from manifest file
     * @throws Exception is thrown when the package name cannot be found.
     */
    private PackageInfo getPackageInfo()
    {
        PackageInfo AppInfo = null;
        try
        {
            AppInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e)
        {
        // TO DO: Change to log instead of printStackTrace
            e.printStackTrace();
        }
        //Return app information from manifest file
        return AppInfo;
    }

    /**
     * This method shows the user the EULA text. If the user accepts the EULA, he/she will be redirected to the
     * app's main menu screen. If the user cancels the EULA, he/she will be presented with the EULA screen again.
     *
     * After the user accepts the EULA once, the EULA screen will not be displayed anymore. However, if there is
     * a version change, the user will be presented with the EULA again.
     *
     */
    public void show()
    {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
        if(hasBeenShown == false)
        {

            // Shows the EULA title and the version information
            // Shows the Eula text (location: res/values/string)
            String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;

            //Shows the EULA text and updates
            //If there are updates, show EULA to user again.
            String message = mActivity.getString(R.string.updates) + "\n\n" + mActivity.getString(R.string.eula);

            // Dialog screen for the EULA
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(message)

                    //Clicking "Ok" on the EULA screen
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        /**
                         * This method remembers if the user accepted the EULA. Once the user accepts
                         * the EULA, the EULA will not be displayed on the succeeding app launch.
                         *
                         * @return void
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })

                    //Clicking "Cancel" on te EULA screen
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
                        /**
                         * This method will close the app if the user clicks on "Cancel" on the EULA
                         * screen.
                         *
                         * @return void
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                            mActivity.finish();
                        }

                    });
            // Create and show EULA dialog box
            builder.create().show();
        }
    }

}