package com.example.group8.reciperescue;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by tomas on 12/1/2015.
 * NOTE
 * INGREDIENT NAMES SHOULD BE UNIQUE
 */
public class IngredientSelection extends Activity {

    // has the custom section been added
    private boolean isCustomAdded = false;

    private final String customHeaderText = "Custom";

    // initialized and wiped on oncreate - only 1
    public static Vector<String> selectedIngredients;

    ExpandableIngredientsListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.ingredientsMainExpandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableIngredientsListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        selectedIngredients = new Vector<String>();

    }

    private void addIngredient(String name) {
        selectedIngredients.add(name);
    }

    private void removeIngredient(String name) {
        for (int i = 0; i < selectedIngredients.size(); i++) {

            if (name.equals(selectedIngredients.elementAt(i))) {
                // found the element we should remove
                selectedIngredients.remove(i);
                break;
            }
        }
    }

    /*
     * Populate the list data - MUST be overridden
     */
    protected void prepareListData() {

    }

    public void ingredientClickedHandler(View v) {
        CheckBox checkBox = (CheckBox)v;
        // this is whether it is NOW checked after the click
        boolean checked = checkBox.isChecked();

        if (checked) {
            // add it to what has been selected
            addIngredient((String)checkBox.getText());
        } else {
            // remove it from what has been selected
            removeIngredient((String)checkBox.getText());
        }
    }

    public void addIngredientClickHandler(View v) {
        if (!isCustomAdded) {
            isCustomAdded = true;
            listDataHeader.add(customHeaderText);
            List<String> custom = new ArrayList<String>();
            listDataChild.put(customHeaderText, custom);
        }
        // get the text to add in
        TextView ingredientView = (TextView)findViewById(R.id.enterIngredientText);
        String ingredientText = ingredientView.getText().toString();
        // empty the string now
        ingredientView.setText("");

        // now has the custom shit ready
        // get ref to array child
        List<String> custom = listDataChild.get(customHeaderText);
        custom.add(ingredientText);
        // also add to ingredients list
        IngredientSelection.selectedIngredients.add(ingredientText);
        listAdapter.notifyDataSetChanged();
    }

    public void findRecipesClickHandler(View v) {
        Intent i=new Intent(this, RecipeCarousel.class);
        startActivity(i);
    }

}
