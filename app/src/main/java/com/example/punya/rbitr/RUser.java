package com.example.punya.rbitr;
import java.util.HashMap;
import java.util.*;

/**
 * Created by Punya on 8/26/16.
 */
public class RUser {
    public String facebookid;
    public int american;
    public int chinese;
    public int french;
    public int indian;
    public int italian;
    public int japanese;
    public int mediterranean;
    public int mexican;
    public int vegan;

    public RUser() {

    }

    public RUser(String userID, int am, int ch, int fr, int in, int it, int ja, int med, int mex, int ve) {
        facebookid = userID;
        american = am;
        chinese = ch;
        french = fr;
        indian = in;
        italian = it;
        japanese = ja;
        mediterranean = med;
        mexican = mex;
        vegan = ve;
    }

    public int getRatingForType(String type) {
        if (type.equalsIgnoreCase("american")) {
            return american;
        }
        if (type.equalsIgnoreCase("chinese")) {
            return chinese;
        }
        if (type.equalsIgnoreCase("french")) {
            return french;
        }
        if (type.equalsIgnoreCase("indian")) {
            return indian;
        }
        if (type.equalsIgnoreCase("italian")) {
            return italian;
        }
        if (type.equalsIgnoreCase("japanese")) {
            return japanese;
        }
        if (type.equalsIgnoreCase("mediterranean")) {
            return mediterranean;
        }
        if (type.equalsIgnoreCase("mexican")) {
            return mexican;
        }
        if (type.equalsIgnoreCase("vegan")) {
            return vegan;
        }
        return 0;
    }

    public HashMap<String, Integer> toMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("american", american);
        map.put("chinese", chinese);
        map.put("french", french);
        map.put("indian", indian);
        map.put("italian", italian);
        map.put("japanese", japanese);
        map.put("mediterranean", mediterranean);
        map.put("mexican", mexican);
        map.put("vegan", vegan);
        return map;
    }

}
