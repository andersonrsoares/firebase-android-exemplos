package br.com.anderson.firebasechat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DevMaker on 10/25/16.
 */

public class Chat {
    String id;
    String name;
    private List<String> users = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
