package com.example.appbanhang.models;

public class HoaDon {
    private String id;
    private String lido;

    private int laisuat;

    public int getLaisuat() {
        return laisuat;
    }

    public void setLaisuat(int laisuat) {
        this.laisuat = laisuat;
    }

    public String getLido() {
        return lido;
    }

    public void setLido(String lido) {
        this.lido = lido;
    }
    public HoaDon() {
    }

    public HoaDon(String id, int tongtien, String ngaydukien, String ngayhoanthanh, String hoten, String sodienthoai, String diachi, String trangthai,String idUser,String lido,int laisuat) {
        this.id = id;
        this.tongtien = tongtien;
        this.ngaydukien = ngaydukien;
        this.ngayhoanthanh = ngayhoanthanh;
        this.hoten = hoten;
        this.sodienthoai = sodienthoai;
        this.diachi = diachi;
        this.trangthai = trangthai;
        this.idUser = idUser;
        this.lido = lido;
        this.laisuat = laisuat;
    }

    private int tongtien;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }

    public String getNgaydukien() {
        return ngaydukien;
    }

    public void setNgaydukien(String ngaytaodon) {
        this.ngaydukien = ngaytaodon;
    }

    public String getNgayhoanthanh() {
        return ngayhoanthanh;
    }

    public void setNgayhoanthanh(String ngayhoanthanh) {
        this.ngayhoanthanh = ngayhoanthanh;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    private String ngaydukien;
    private String ngayhoanthanh;
    private String hoten;
    private String sodienthoai;
    private String diachi;
    private String trangthai;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    private String idUser;
}
