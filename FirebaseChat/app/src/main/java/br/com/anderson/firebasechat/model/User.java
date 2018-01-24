package br.com.anderson.firebasechat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DevMaker on 10/24/16.
 */

public class User implements Serializable{
    private String id;
    private String name;
    private String email;
    private List<String> groups = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
