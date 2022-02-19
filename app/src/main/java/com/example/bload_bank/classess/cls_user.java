package com.example.bload_bank.classess;

public class cls_user {
    String hid,uname,phone,location,email,img,blooadgroup;

    public cls_user() {

    }

    public cls_user(String hid, String uname, String phone, String location, String email, String img, String blooadgroup) {
        this.hid = hid;
        this.uname = uname;
        this.phone = phone;
        this.location = location;
        this.email = email;
        this.img = img;
        this.blooadgroup = blooadgroup;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBlooadgroup() {
        return blooadgroup;
    }

    public void setBlooadgroup(String blooadgroup) {
        this.blooadgroup = blooadgroup;
    }
}
