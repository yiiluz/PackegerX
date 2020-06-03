package com.example.packegerv2.Entities;

public class Person {
    private String _firstNAme;
    private String _lastName;
    private String _email;
    private String _phoneNumber;
    private String _photoUrl;
    private String _userID;
    private Address _address;

    public Person() {
    }

    public Person(String _firstNAme, String _lastName, String _email, String _phoneNumber, String _photoUrl, String _userID, Address address) {
        this._firstNAme = _firstNAme;
        this._lastName = _lastName;
        this._email = _email;
        this._phoneNumber = _phoneNumber;
        this._photoUrl = _photoUrl;
        this._userID = _userID;
        this._address = address;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_firstNAme() {
        return _firstNAme;
    }

    public void set_firstNAme(String _firstNAme) {
        this._firstNAme = _firstNAme;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_photoUrl() {
        return _photoUrl;
    }

    public void set_photoUrl(String _photoUrl) {
        this._photoUrl = _photoUrl;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public String get_userID() {
        return _userID;
    }

    public void set_userID(String _userID) {
        this._userID = _userID;
    }

    public Address getAddress() {
        return _address;
    }

    public void setAddress(Address address) {
        this._address = address;
    }
}
