package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NamedQueries(
        {@NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
         @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u.username = :username")
@NamedQueries({
        @NamedQuery(name="userByUserId", query = "select u from UserEntity u where u.uuid=:userId")
        })
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 200)
    private String uuid;

    @NotNull
    @Size(max = 30)
    private String firstname;

    @NotNull
    @Size(max = 30)
    private String lastname;

    @NotNull
    @Size(max = 30)
    private String username;

    @NotNull
    @Size(max = 255)
    private String email;

    @ToStringExclude
    private String password;

    @Size(max = 200)
    @ToStringExclude
    private String salt;

    @Size(max = 30)
    private String country;

    @Size(max = 50)
    private String aboutme;

    @Size(max = 30)
    private String dob;

    @Size(max = 30)
    private String role;

    @Size(max = 30)
    private String contactnumber;


    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

}