package com.example.firstrestapi.Records.Forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RegisterForm {
    private String _prename;
    private String _name;
    private String _email;
    private String _password;
    private String _password_confirm;
    private Date _birth;

    public String prename() {return _prename;}
    public String name() {return _name;}
    public String email() {return _email;}
    public String password() {return _password;}
    public String password_confirm() {return _password_confirm;}
    public Date birth() {return _birth;}
    public RegisterForm(String prename, String name, String email, String password, String password_confirm, String birth) throws ParseException {
        this._prename = prename;
        this._name = name;
        this._email = email;
        this._password = password;
        this._password_confirm = password_confirm;
        SimpleDateFormat format = new SimpleDateFormat("MM/DD/YYYY");
        this._birth = format.parse(birth);
    }
}
