package com.example.punya.rbitr;
/**
 * Created by Punya on 8/29/16.
 */
public class Friend {
    private String name;
    private String id;

    public Friend (String myname, String myid) {
        name = myname;
        id = myid;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() { return name + " " + id; }
}
