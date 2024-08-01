package com.example.appbanhang.models;

public class ThuongHieu {
    public void setHinhTH(String hinhTH) {
        HinhTH = hinhTH;
    }

    private String ID;
    private String TenTH;
    private String HinhTH;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenTH() {
        return TenTH;
    }

    public void setTenTH(String tenTH) {
        TenTH = tenTH;
    }

    public String getHinhTH() {
        return HinhTH;
    }

    public ThuongHieu(String ID, String tenTH, String hinhTH) {
        this.ID = ID;
        this.TenTH = tenTH;
        this.HinhTH = hinhTH;
    }
}
