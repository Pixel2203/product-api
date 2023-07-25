package com.example.firstrestapi.DTOs;

public class UserDTO {
    private int id;
    private String vorname;
    private String nachname;
    private String email;

    public UserDTO(int id, String vorname,String nachname, String email){
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getVorname(){
        return this.vorname;
    }
    public String getNachname(){return this.nachname;}
    public String getEmail(){
        return this.email;
    }
    public void setName(String name){
        this.vorname = name;
    }
}
