package com.example.appbanhang.models;

public class LichSuTruyCap {
    public LichSuTruyCap(String tenUser,String sodienthoaiUser, String ngaygio, String trangthai) {
        this.tenUser = tenUser;
        this.sodienthoaiUser = sodienthoaiUser;
        this.ngaygio = ngaygio;
        this.trangthai = trangthai;
    }


    public String getNgaygio() {
        return ngaygio;
    }

    public void setNgaygio(String ngaygio) {
        this.ngaygio = ngaygio;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getTenUser() {
        return tenUser;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
    }

    public String getSodienthoaiUser() {
        return sodienthoaiUser;
    }

    public void setSodienthoaiUser(String sodienthoaiUser) {
        this.sodienthoaiUser = sodienthoaiUser;
    }

    private String tenUser;
    private String sodienthoaiUser;
    private String ngaygio;
    private String trangthai;
}
