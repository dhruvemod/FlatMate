package com.example.codersdelight.flatmate;

/**
 * Created by codersdelight on 4/4/17.
 */

public class chatMessages {
    private String name;
    private String note;
    private String message;
public chatMessages(){
}
public chatMessages(String message,String name){
    this.name=name;
    this.message=message;
}
public chatMessages(String message,String name,String note){
    this.name=name;
    this.message=message;
    this.note=note;
}

public String getName(){
    return name;
}
public void setName(String name){
    this.name=name;
}
public String getMessage(){
    return message;
}
public void setMessage(String message){
    this.message=message;
}
public String getNote(){
    return note;
}
public void setNote(String note){
    this.note=note;
}
}

