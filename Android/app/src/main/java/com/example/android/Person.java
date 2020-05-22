package com.example.android;

public class Person {
    private String name;
    private String Lastname;
    private String email;
    private String password;



    public Person(String name, String Lastname, String email, String password) {
        this.name = name;
        this.Lastname = Lastname;
        this.email = email;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
