package com.org.pawpal.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.pawpal.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hp-pc on 11-01-2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public static int ITEM1 = 0;
    public static int ITEM2 = 1;
    public static int ITEM3 = 2;
    public static int ITEM4 = 3;
    public static int ITEM5 = 4;


    public static int SUBITEM3_1 = 0;
    public static int SUBITEM3_2 = 1;
    public static int SUBITEM3_3 = 2;




    public ExpandableListAdapter(Context context, List<String> expandableListTitle,
                                 HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;


    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        ImageView ivMenuChild = (ImageView) convertView
                .findViewById(R.id.iv_menu_child_icon);
        if (expandedListPosition ==0)
            ivMenuChild.setImageResource(R.mipmap.inbox);
        else if (expandedListPosition == 1)
            ivMenuChild.setImageResource(R.mipmap.sent);
        else if (expandedListPosition == 2)
            ivMenuChild.setImageResource(R.mipmap.archieve);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(listTitle);



        // set icons for menu items
        ImageView ivMenu = (ImageView) convertView
                .findViewById(R.id.iv_menu_icon);
        if (listPosition ==0)
            ivMenu.setImageResource(R.mipmap.dashboard);
        else if (listPosition == 1)
            ivMenu.setImageResource(R.mipmap.icon_findpal);
        else if (listPosition == 2)
            ivMenu.setImageResource(R.mipmap.messages);
        else if (listPosition == 3)
            ivMenu.setImageResource(R.mipmap.icon_pawfile_menu);
        else if (listPosition == 4)
            ivMenu.setImageResource(R.mipmap.signout);

     /*   listTitleTextIconView.setTypeface(null, Typeface.NORMAL);
        if (listPosition == ITEM1)
            listTitleTextIconView.setText("glass");
        else if (listPosition == ITEM2)
            listTitleTextIconView.setText("music");
        else if (listPosition == ITEM3)
            listTitleTextIconView.setText("search");
        else if (listPosition == ITEM4)
            listTitleTextIconView.setText("envelope");*/

        // set arrow icons for relevant items
       /* if (listPosition == ITEM1 || listPosition == ITEM2) {
            if (!isExpanded)
                listTitleTextArrowView.setText("fa_chevron_right");
            else
                listTitleTextArrowView.setText("fa_chevron_down");
        } else {
            listTitleTextArrowView.setText("");

        }*/
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
