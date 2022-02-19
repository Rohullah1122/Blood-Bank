package com.example.bload_bank.classess;

import android.util.Log;

public class cls_msg {

    private  String messagesID,senderID,imageUrl,msgs,userimg,username;
    Long date;

    public cls_msg(){}

//    public cls_msg(String msgs,String senderID,long date){
//        this.msgs = msgs;
//        this.senderID = senderID;
//        this.date = date;
//    }

    public cls_msg(String senderID, String msgs, String userimg, String username, Long date) {
        this.senderID = senderID;
        this.msgs = msgs;
        this.userimg = userimg;
        this.username = username;
        this.date = date;
    }


    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getMessagesID() {
        return messagesID;
    }

    public void setMessagesID(String messagesID) {
        this.messagesID = messagesID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsgs() {
        return msgs;
    }

    public void setMsgs(String msgs) {
        this.msgs = msgs;
    }
}
