package com.example.group8.reciperescue;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tomas on 12/1/2015.
 * example from http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */
public class ExpandableIngredientsListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableIngredientsListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ingredients_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.ingredientSelectionListItem);

        txtListChild.setText(childText);

        // fix checkbox bug
        setCheckBoxIfIngredientIsSelected((CheckBox)txtListChild, childText);

        return convertView;
    }

    /*
    FOR BUG WHERE RANDOM SHIT GETS CHECKED WHEN COLLAPSING LISTS THAY HAVE BEEN CHECKED
     */
    public void setCheckBoxIfIngredientIsSelected(View v, String childText) {
        // set the checkbox to checked or not checked
        CheckBox checkbox = (CheckBox)v;

        int found = IngredientSelection.selectedIngredients.indexOf(childText);

        if (found < 0) {
            // The ingredient has not been selected
            checkbox.setChecked(false);
        } else {
            // the ingredient has been selected
            checkbox.setChecked(true);
        }
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ingredients_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.ingredientSelectionListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
