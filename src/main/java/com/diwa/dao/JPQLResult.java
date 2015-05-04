package com.diwa.dao;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by josemo on 5/3/15.
 */
public class JPQLResult {

    private String query;

    private Map<String, Serializable> parameters;

    public JPQLResult(String query, Map<String, Serializable> parameters){
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery () {
        return query;
    }

    public Map<String, Serializable> getParameters () {
        return parameters;
    }
}
