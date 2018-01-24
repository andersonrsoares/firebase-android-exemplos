package br.com.anderson.firebasechat.model;

import java.io.Serializable;

/**
 * Created by DevMaker on 10/24/16.
 */

public class Group implements Serializable{
    String id;
    String name;

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
}
