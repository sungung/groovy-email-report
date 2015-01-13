package com.sungung.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author parks
 * @since 12/01/15 9:09 AM
 */
@Entity
public class Brewer implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String contact;

    @Column
    private String address;

    @Column
    private String suburb;

    @Column(name = "postcode")
    private String postCode;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "website")
    private String webSite;

    @Column(name = "operhours")
    private String operHours;

    @Column(name = "beerselection")
    private String beerSelection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getOperHours() {
        return operHours;
    }

    public void setOperHours(String operHours) {
        this.operHours = operHours;
    }

    public String getBeerSelection() {
        return beerSelection;
    }

    public void setBeerSelection(String beerSelection) {
        this.beerSelection = beerSelection;
    }

}
