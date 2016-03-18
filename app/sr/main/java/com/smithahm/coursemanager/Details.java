package com.smithahm.coursemanager;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Details")
public class Details extends ParseObject {

    public Details(){
        super();
    }

    public String getCourseId() {
        return getString("courseId");
    }
    public void setCourseId(String id) {
        put("courseId", id);
    }

    public String getContent() {
        return getString("content");
    }
    public void setContent(String content) {
        put("content", content);
    }

    public Date getSubDate() {
        return getDate("subDate");
    }
    public void setSubDate(Date date) {
        put("subDate", date);
    }

    public Boolean getPartner() {
        return getBoolean("partner");
    }
    public void setPartner(Boolean partner) {
        put("partner", partner);
    }

    public ParseFile getFile() {
        return getParseFile("attachment");
    }
    public void setFile(ParseFile file) {
        put("attachment", file);
    }

    public String getFileName() {
        return getString("filename");
    }
    public void setFileName(String name) {
        put("filename", name);
    }
}
