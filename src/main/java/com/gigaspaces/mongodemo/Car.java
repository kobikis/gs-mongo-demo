package com.gigaspaces.mongodemo;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Created by kobi on 1/19/14.
 */
public class Car {

    private Integer id;
    private String name;
    private Integer model;

    @SpaceId
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }
}
