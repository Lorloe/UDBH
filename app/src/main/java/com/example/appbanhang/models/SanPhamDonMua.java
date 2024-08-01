package com.example.appbanhang.models;

public class SanPhamDonMua {
    public SanPhamDonMua(String ten, String hinh, int gia, int soluong) {
        this.ten = ten;
        this.hinh = hinh;
        this.gia = gia;
        this.soluong = soluong;
    }

    public SanPhamDonMua() {
    }

    private String ten;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    private String hinh;
    private int gia;
    private int soluong;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
