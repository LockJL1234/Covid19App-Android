package com.example.covid19app;

//java class for user information
public class ProfileHelperClass {

    String username, email, age, school;

    public ProfileHelperClass(String username, String school, String email, String age) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.school = school;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
