package com.example.appbanhang.models;

public class SanPham {
    private String idSP;
    private int giaGoc;

    public int getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(int giaGoc) {
        this.giaGoc = giaGoc;
    }

    public String getIdSP() {
        return idSP;
    }

    private void setIdSP(String idSP) {
        this.idSP = idSP;
    }

    private String tenSP;
    private String hinhSP;
    private int giaSP;
    private boolean isYeuThich;

    public int getSoluongKho() {
        return soluongKho;
    }

    public void setSoluongKho(int soluongKho) {
        this.soluongKho = soluongKho;
    }

    private int soluongKho;
    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    private int soluong;

    public boolean isYeuThich() {
        return isYeuThich;
    }
    public void setYeuThich(boolean yeuThich) {
        isYeuThich = yeuThich;
    }
    private String motaSP;
    private String idTH;

    public String getIdTH() {
        return idTH;
    }

    public void setIdTH(String idTH) {
        this.idTH = idTH;
    }

    public String getMotaSP() {
        return motaSP;
    }

    public void setMotaSP(String motaSP) {
        this.motaSP = motaSP;
    }

    public SanPham(String idSP, String tenSP, String hinhSP, int giaSP, String motaSP,String idTH,int soluongKho,int giaGoc) {
        this.idSP = idSP;
        this.tenSP = tenSP;
        this.hinhSP = hinhSP;
        this.giaSP = giaSP;
        this.motaSP = motaSP;
        this.idTH = idTH;
        this.soluongKho = soluongKho;
        this.giaGoc = giaGoc;
    }
    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }
}
