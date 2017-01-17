package com.org.pawpal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by hp-pc on 11-01-2017.
 */
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> menu3 = new ArrayList<String>();
        List<String> menu4 = new ArrayList<String>();
        List<String> menu5 = new ArrayList<String>();

        List<String> list1 = new ArrayList<String>();


        List<String> list2 = new ArrayList<String>();

        menu3.add("Inbox");
        menu3.add("Sent");
        menu3.add("Archive");


        expandableListDetail.put("Dashboard", list1);
        expandableListDetail.put("Find Pal", list2);
        expandableListDetail.put("Messages", menu3);
        expandableListDetail.put("PawFile", menu4);
        expandableListDetail.put("Signout", menu5);


        return expandableListDetail;
    }
}
