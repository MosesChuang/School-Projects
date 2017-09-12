package com.example.group8.reciperescue;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class puts together the list of food ingredients items that the user could have at home.
 * The ingredients will be displayed in a drop down menu list format with check boxes next to the
 * items that have been selected by the user
 * @author Tomas Re & Jeffrey Noehren
 * @date 12/1/2015.
 */
public class FoodIngredientSelection extends IngredientSelection {

    /**
     * When this activity is called the image view on the top is changed to the image of our food
     * chef avatar anf the background is set to the food page layout
     * @param savedInstanceState Bundle
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView chefImage = (ImageView)findViewById(R.id.ingredientsListChefImageView);
        chefImage.setImageResource(R.drawable.food_page);

    }
    /**
     *This method adds the different types of food ingredents that the user has in their house
     * listed under the heading fruits, vegetables, meats and beans, grains, and spices.
     * */
    @Override
    protected void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Add set of data
        String fruit = "Fruits";
        listDataHeader.add(fruit);
        List<String> fruits = new ArrayList<String>();
        fruits.add("Banana");
        fruits.add("Apple");
        fruits.add("Orange");
        fruits.add("Grapes");
        fruits.add("Mango");
        fruits.add("Strawberry");
        fruits.add("Blueberry");
        fruits.add("Pineapple");
        fruits.add("Kiwi");
        fruits.add("Cherry");
        fruits.add("Lemon");
        fruits.add("Lime");
        fruits.add("Peach");
        fruits.add("Watermellon");
        fruits.add("Pomegranate");

        String veggie = "Vegetables";
        listDataHeader.add(veggie);
        List<String> veggies = new ArrayList<String>();
        veggies.add("Lettuce");
        veggies.add("Cucumber");
        veggies.add("Tomatoe");
        veggies.add("Squash");
        veggies.add("Onion");
        veggies.add("Artichoke");
        veggies.add("Broccoli");
        veggies.add("Carrot");
        veggies.add("Asparagus");
        veggies.add("Mushrooms");
        veggies.add("Spinach");
        veggies.add("Corn");
        veggies.add("Kale");
        veggies.add("Eggplant");
        veggies.add("Bell Pepper");

        String meat = "Meats and Beans";
        listDataHeader.add(meat);
        List<String> meats = new ArrayList<String>();
        meats.add("Steak");
        meats.add("Beef");
        meats.add("Chicken");
        meats.add("Veal");
        meats.add("Salmon");
        meats.add("Shrimp");
        meats.add("Pork");
        meats.add("Crab");
        meats.add("Turkey");
        meats.add("Bacon");
        meats.add("Black Beans");
        meats.add("Kidney Beans");
        meats.add("Chickpeas");
        meats.add("White Beans");
        meats.add("Lentils");
        meats.add("Pinto Beans");
        meats.add("Black Eyed Pea");


        String Grains = "Grains";
        listDataHeader.add(Grains);
        List<String> grains = new ArrayList<String>();
        grains.add("Spaghetti");
        grains.add("Lingine");
        grains.add("Elbow Macaroni");
        grains.add("Rigatoni");
        grains.add("White Rice");
        grains.add("Brown Rice");
        grains.add("Whole Wheat Bread");
        grains.add("White Bread");
        grains.add("Quinoa");
        grains.add("Flour");
        grains.add("Baggett");

        String Dairy = "Dairy";
        listDataHeader.add(Dairy);
        List<String> dairy = new ArrayList<String>();
        dairy.add("Milk");
        dairy.add("White Cheddar Cheese");
        dairy.add("Sharp Cheddar Cheese");
        dairy.add("Mozzarella");
        dairy.add("Jack Cheese");
        dairy.add("Gouda");
        dairy.add("Swiss Cheese");
        dairy.add("Parmesan Cheese");

        String Spices = "Spices";
        listDataHeader.add(Spices);
        List<String> spices = new ArrayList<String>();
        spices.add("Salt");
        spices.add("Black Pepper");
        spices.add("White Pepper");
        spices.add("White Sugar");
        spices.add("Brown Sugar");
        spices.add("Powdered Sugar");
        spices.add("Curry Powder");
        spices.add("Cumin");
        spices.add("Cayenne Pepper");
        spices.add("Chili Powder");
        spices.add("Garlic Powder");
        spices.add("All Spice");
        spices.add("Graham Marsala");
        spices.add("Tumeric");
        spices.add("Nutmeg");
        spices.add("Oregano");

        listDataChild.put(fruit, fruits);
        listDataChild.put(veggie, veggies);
        listDataChild.put(meat, meats);
        listDataChild.put(Grains, grains);
        listDataChild.put(Dairy, dairy);
        listDataChild.put(Spices, spices);

    }
}
