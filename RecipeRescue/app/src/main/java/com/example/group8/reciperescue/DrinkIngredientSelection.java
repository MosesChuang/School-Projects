package com.example.group8.reciperescue;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class puts together the list of drink ingredients items that the user could have at home.
 * The ingredients will be displayed in a drop down menu list format with check boxes next to the
 * items that have been selected by the user
 * @author Tomas Re & Jeffrey Noehren
 * @date 12/1/2015.
 */
public class DrinkIngredientSelection  extends IngredientSelection{

    /**
     * When this activity is called the image view on the top is changed to the image of our drinks
     * bartender avatar anf the background is set to the beverage page layout
     * @param savedInstanceState Bundle
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView chefImage = (ImageView)findViewById(R.id.ingredientsListChefImageView);
        chefImage.setImageResource(R.drawable.beverage_page);
    }

    /**
     *This method adds the different types of drink ingredents that the user has in their house
     * listed under the heading rum, tequila, whiskey, liqueur, gin, soda, and juice.
     * */
    @Override
    protected void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Add first group of data
        String Rum = "Rum";
        listDataHeader.add(Rum);
        List<String> rum = new ArrayList<String>();
        rum.add("Light Rum");
        rum.add("Dark Rum");
        rum.add("Malabu Rum");
        rum.add("Spiced Rum");

        String Tequila = "Tequila";
        listDataHeader.add(Tequila);
        List<String> tequila = new ArrayList<String>();
        tequila.add("Silver Tequila");
        tequila.add("Gold Tequila");
        tequila.add("Mezcal");

        String Whiskey = "Whisky";
        listDataHeader.add(Whiskey);
        List<String> whiskey = new ArrayList<String>();
        whiskey.add("Burbon");
        whiskey.add("Rye");
        whiskey.add("Scotch");

        String Liqueur = "Liqueur";
        listDataHeader.add(Liqueur);
        List<String> liqueur = new ArrayList<String>();
        liqueur.add("Jägermeister");
        liqueur.add("Limmoncello");
        liqueur.add("Apple Schnapps");
        liqueur.add("Peach Schnapps");
        liqueur.add("Kahula");
        liqueur.add("Baileys");

        String Gin = "Gin";
        listDataHeader.add(Gin);
        List<String> gin = new ArrayList<String>();
        gin.add("London Dry");
        gin.add("New American");

        String Soda = "Soda";
        listDataHeader.add(Soda);
        List<String> soda = new ArrayList<String>();
        soda.add("Coke");
        soda.add("Sprite");
        soda.add("Dr. Pepper");
        soda.add("Orange Soda");
        soda.add("Grape Soda");
        soda.add("Mountain Due");

        String Juice = "Juice";
        listDataHeader.add(Juice);
        List<String> juice = new ArrayList<String>();
        juice.add("Apple Juice");
        juice.add("Lemon Juice");
        juice.add("Cranberry Juice");
        juice.add("Grape Juice");
        juice.add("Orange Juice");
        juice.add("Carrot Juice");
        juice.add("Pineapple Juice");
        juice.add("Peach Nectar");

        // put the data in the list
        listDataChild.put(Rum, rum);
        listDataChild.put(Tequila, tequila);
        listDataChild.put(Whiskey, whiskey);
        listDataChild.put(Liqueur, liqueur);
        listDataChild.put(Gin, gin);
        listDataChild.put(Soda, soda);
        listDataChild.put(Juice, juice);

    }
}
