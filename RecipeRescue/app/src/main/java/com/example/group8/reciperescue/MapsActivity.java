/**
 * MapActivity Class
 * This class is where all map related work will be stored. Such features will include current location and getting directions
 *
 * @author Kevin Fang
 * @version 1.1
 */

package com.example.group8.reciperescue;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A Map will be created,displayed, and a marker will be placed at your current location on the map.
 * The map will preload nearby grocery stores and supermarkets based on your location
 *@author Kevin Fang
 *@version 1.1
 */
public class MapsActivity extends FragmentActivity {

    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String GOOGLE_API_KEY = "AIzaSyB-rpBp97WK5LKF5QNnrNcjjM6R_izsU7U";
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
     MapWrapperLayout mapWrapperLayout;

    /**
     * When started, this function starts the map with preset settings
     * @param savedInstanceState, Bundle, The instance of the map that was previously saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //checks if they have google services on
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        placeText = (EditText) findViewById(R.id.placeText);

        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_wrapper);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        mMap = supportMapFragment.getMap();
        setUpMap();

        placeFinder();

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.infowindow_layout, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        //this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
               ContextCompat.getDrawable(this, R.drawable.common_signin_btn_icon_dark),
                ContextCompat.getDrawable(this,R.drawable.common_signin_btn_icon_light))
        {
            /**
             * When marker is clicked display name, address, and the directions button
             * @param v, View, The view that was clicked inside the window
             * @param marker, Marker, The marker that was clicked will display the info
             */
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(MapsActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
                String geoUriString = "http://maps.google.com/maps?saddr=" +
                        latitude + "," +
                        longitude + "&daddr=" +
                        marker.getPosition().latitude + "," +
                        marker.getPosition().longitude;

                // Call for Google Maps to open
                Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
                startActivity(mapCall);
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            /**
             * Gets the information window
             * @param marker, Marker, When marker is clicked gets the info window
             * @return View, returns 
             */
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            /**
             * Gets the contents of the information window
             * @param marker, Marker, this is the marker that was selected for its information
             * @return View, this is the window that contains information regarding that marker
             */
            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
                infoButtonListener.setMarker(marker);
                TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);
                markerLabel.setText(marker.getTitle());

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
    }

    /**
     * Gets and returns the size for the view of marker and info window for MapWrapperLayout
     * @param context, Context, This is used when creating views, in our case we are creating, context of the environment
     * @param dp, float, pixel resizing
     * @return int, value that was calculated for the dimensions of the window
     */
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }


    /**
     *This will "resume" the map to it's previous state before you left the screen. It will load anything you previously had on the map.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so and the map has not already been instantiated.. This will ensure that we only ever
     * call setupmap() once and when mMap is not null.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can put any map specifications that we want.
     * <p>
     *     The function is called at the beginning in on create. Once called the function will set up the map and place a pin on your current location and zoom in to a city view. It will also check to make sure it has permission.
     * </p>
     *
     */
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(true);

        //lets you see current traffic
       // mMap.setTrafficEnabled(true);


        mMap.setIndoorEnabled(true);

        //buildings are displayed
        mMap.setBuildingsEnabled(true);

        //allows user to zoom in and out with button
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        System.out.println(provider);

        // checks for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (myLocation != null) {
            // Get latitude of the current location
             latitude = myLocation.getLatitude();

            // Get longitude of the current location
             longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location"));

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            //will set zoom level to the specified number, 14 being city level
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            mMap.animateCamera(cameraUpdate);
        }
    }



    /**
     * Function that checks for google play services
     * @return boolean, will check if google play services is on, if not it will shut down
     */
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * When clicked place finder will use string to get locations
     * <p>
     *    A string is first built, then a new placetask will be created and placetask will then execute using the formatted string.
     * </p>
     */
    public void placeFinder (){
        StringBuilder sbValue = new StringBuilder(stringBuildingMethod());
        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sbValue.toString());
    }

    /**
     * stringBuildingMethod prebuilds the string needed to get the json file.
     * @return string, returns the string formatted.
     */
    public StringBuilder stringBuildingMethod() {
        StringBuilder stringBuild = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=");
        /*
        This segment builds the string that we use to retrieve the json file and its contents
         */
        stringBuild.append("&location=" + latitude + "," + longitude);
        stringBuild.append("&radius=5000");
        stringBuild.append("&types=" + "grocery_or_supermarket");
        stringBuild.append("&sensor=true");
        stringBuild.append("&key=AIzaSyBjtxXfW2QlQHHOAUp4HWVPMUC-X1cBhV4");
        System.out.println(stringBuild);
        Log.d("Map", "api: " + stringBuild.toString());

        return stringBuild;
    }

    /**
     * PlacesTask does two things, doinbackground and onpostexecute
     * <p>
     *     PlacesTask will take a url and retrieve data from it and then it will parse that retrieved information
     * </p>
     * @author Kevin
     * @version 1.1
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        /**
         * The function happens behind the scenes and basically retrieves information from a url.
         * @param url, String,  doInBackground takes in a url string
         * @return string, returns data that was pulled from the url
         */
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        /**
         * The function will parse the data taken from the url
         * @param result, String, The result is the data retrieved from the url
         */
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    /**
     * downloadUrl takes in a urlstring and connects to a connection then reads through and passes everything through a buffer
     * @param strUrl, String, passing in the url string
     * @return String, returns the newly edited string after all the lines have been buffered
     * @throws IOException
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            System.out.println(data);
            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * PaserTask will return a list of places and then each place will be marked after the list is returned
     * @author Kevin
     * @version 1.1
     */

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        /**
         * When executed, doInBackground will take the location from the jsonObject in the JsonData[]
         * @param jsonData, String, data from the jason file.
         * @return string, after parsing the json object, we return the string we get.
         */
        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method

        /**
         * When onPostExecute is ran it will go through the list of places and create location Markers for each one as well as a title
         * @param list, List<HashMap<String, String>>, list of all the places
         */
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers; so new search can be made
            mMap.clear();

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your current Location"));

            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(list.size());

            //run i times for i places found
            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                final String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);

                // Getting vicinity
                final String Location = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    /**
                     * Gets the info window for the specific marker
                     * @param marker, Marker, the marker that was selected for its info window
                     * @return null, returns null
                     */
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    /**
                     * Gets content for the info window of the selected marker.
                     * @param marker, Marker, the marker we need to get the infow window for
                     * @return infoWindow, returns the info window for the selected marker
                     */

                    @Override
                    public View getInfoContents(Marker marker) {
                        // Setting up the infoWindow with current's marker info
                        //View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
                        infoButtonListener.setMarker(marker);
                        TextView markerLabel = (TextView)infoWindow.findViewById(R.id.marker_label);
                        markerLabel.setText(marker.getTitle());

                        // We must call this to set the current marker and infoWindow references
                        // to the MapWrapperLayout
                        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                        return infoWindow;
                    }
                });

                //adds name and location
                markerOptions.title(name + "\n" + Location);

                //changes the marker of the new restaurants to a different icon
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                //adds the marker for the current restaurant
                Marker m = mMap.addMarker(markerOptions);
            }
        }
    }
    /**
     * Will be able to take information from a JObject and eventually parse all data from a JSONObject into a HashMap
     * @author Kevin
     * @version 1.1
     */
    public class Place_JSON {

        /**
         * Receives a JSONObject and returns a list
         * @param jObject, JSONObject, a jsonobject that contains the places
         * @return JSONArray, the array of json object where each json object represent a place
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                // Retrieves all the elements in the 'places' array
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Invoking getPlaces with the array of json object  where each json object represent a place
            return getPlaces(jPlaces);
        }

        /**
         * Adds each place in the JSONArray to PlaceList after parsing each place
         * @param jPlaces, JSONArray, an array that contains all the places
         * @return List<HashMap<String, String>>, returns arrayList of places
         */
        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            // Taking each place, parses and adds to list object
            for (int i = 0; i < placesCount; i++) {
                try {
                    // Call getLocation with place JSON object to parse the place
                    place = getLocation((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         * @param jPlace, JSONArray, an array that contains all the places
         * @return HashMap<String, String>, returns a hashmap of the places that contain  information such as lat and lng
         */
        private HashMap<String, String> getLocation(JSONObject jPlace) {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "NA";
            String Location = "NA";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    Location = jPlace.getString("vicinity");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");

                place.put("place_name", placeName);
                place.put("vicinity", Location);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }

}