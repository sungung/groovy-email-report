package com.sungung.data.service;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author PARK Sungung
 * @since 0.0.1
 */
public class BrewerSearchCriteria implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;

    public BrewerSearchCriteria() {
    }

    public BrewerSearchCriteria(String name) {
        Assert.notNull(name, "Brewer name cannot be empty");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
