package com.dev.ettee;

public class User {
    int id;
    String username = null;
    String password;
    String firstname = null;
    String lastname = null;
    String email;
    Address address = null;

    public User() {
        this.id = 1;
        this.username = "PaulDT";
        this.password = "passsword1";
        this.firstname = "Paul";
        this.lastname = "Du tronc";
        this.email = "paul@mail.fr";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
