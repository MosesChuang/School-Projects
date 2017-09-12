package com.example.group8.reciperescue;

import java.util.Vector;

/**
 * Class for getting a dummy list of RecipeMessageObject for developing UI
 * @author Tomas Re
 * @date 11/10/2015
 * @version 1.0
 */
public class RecipeMessageObjectDummy {

        /**
         * Creates a list of dummy RecipeMessageObjects
         * @return Vector<RecipeMessageObject> array of RecipeMessageObjects
         */
        public static Vector<RecipeMessageObject> getRecipeList() {
                Vector<RecipeMessageObject> dummyList = new Vector<>(3);

                Vector<String> ingredientList1 = new Vector<>();
                ingredientList1.addElement("SALT");
                ingredientList1.addElement("PEPPER");
                dummyList.addElement(new RecipeMessageObject(
                        "123123",
                        "S",
                        "WOW PASTA",
                        ingredientList1,
                        "example.com/recipe",
                        25,
                        "image"
                ));

                Vector<String> ingredientList2 = new Vector<>();
                ingredientList2.addElement("SALTyyy");
                ingredientList2.addElement("PEPPERaaaa");
                dummyList.addElement(new RecipeMessageObject(
                        "12312242324",
                        "S",
                        "WOW PASTA v2",
                        ingredientList2,
                        "example.com/recipe2",
                        44,
                        "image"
                ));

                Vector<String> ingredientList3 = new Vector<>();
                ingredientList3.addElement("pasta");
                ingredientList3.addElement("water");
                ingredientList3.addElement("milk");
                dummyList.addElement(new RecipeMessageObject(
                        "12312222242324",
                        "Y",
                        "WOW PASTA v3",
                        ingredientList3,
                        "example.com/recipe3",
                        75,
                        "image"
                ));

                return dummyList;
        }
}