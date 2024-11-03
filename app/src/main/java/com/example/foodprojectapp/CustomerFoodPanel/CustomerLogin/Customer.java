package com.example.foodprojectapp.CustomerFoodPanel.CustomerLogin;

public class Customer {


    private String City, Area, ConfirmPassword, EmailID, FirstName, LastName, Mobileno, Password, State, Suburban, LocalAddress;

    public Customer() {


    }

    public Customer(String City, String Area, String confirmPassword, String emailID, String firstName, String lastName, String mobileno, String password, String state, String suburban, String localAddress) {
        this.City = City;
        Area = Area;
        ConfirmPassword = confirmPassword;
        EmailID = emailID;
        FirstName = firstName;
        LastName = lastName;
        Mobileno = mobileno;
        Password = password;
        State = state;
        Suburban = suburban;
        LocalAddress = localAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() { return Area; }

    public void setArea(String area) {Area = area;}

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobileno() {
        return Mobileno;
    }

    public void setMobileno(String mobileno) {
        Mobileno = mobileno;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getSuburban() {
        return Suburban;
    }

    public void setSuburban(String suburban) {
        Suburban = suburban;
    }

    public String getLocalAddress() {
        return LocalAddress;
    }

    public void setLocalAddress(String localAddress) {
        LocalAddress = localAddress;
    }
}
