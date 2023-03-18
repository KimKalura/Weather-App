package com.spring.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq",
            sequenceName = "user_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @ManyToMany(mappedBy = "userList", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("userList")
    private List<City> cityList;

    @ManyToMany
    @JsonIgnoreProperties("userList")
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList;


    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<City> getCityList() {
        if (cityList == null) {
            cityList = new ArrayList<>();
        }
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Role> getRoleList() {
        if (roleList == null) {
            roleList = new ArrayList<>();
        }
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
