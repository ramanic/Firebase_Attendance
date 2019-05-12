package com.ramanic.attendance;

public class Students {
    String name, pass, std_class,id;

    public Students() {
    }
    public String getName() {
        return name;
    }
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getclass() {
        return std_class;
    }
    public void setclass(String std_class) {
        this.std_class = std_class;
    }
}