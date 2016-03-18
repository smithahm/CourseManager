package com.smithahm.coursemanager;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Course")
public class Course extends ParseObject {

    public Course(){
        super();
    }

    public String getCourseId() {
        return getString("courseId");
    }
    public void setCourseId(String id) {
        put("courseId", id);
    }

    public String getTitle() {
        return getString("title");
    }
    public void setTitle(String title) {
        put("title", title);
    }

    public String getOwner() {
        return getString("owner");
    }
    public void setOwner(ParseUser owner) {
        put("owner", owner);
    }

}
