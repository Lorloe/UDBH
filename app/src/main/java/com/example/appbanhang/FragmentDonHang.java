package com.example.appbanhang;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.ChiTietHoaDon;
import com.example.appbanhang.models.HoaDon;
import com.example.appbanhang.models.SanPham;
import com.example.appbanhang.models.SanPhamDonMua;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class FragmentDonHang extends Fragment {
    DatabaseReference referenceHD;
    RecyclerView recyclerView;
    public static ArrayList<HoaDon> listSanPhamDonHang  = new ArrayList<HoaDon>();
    RecyclerViewDonHang adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tiendogiaohang,container,false);
        recyclerView = view.findViewById(R.id.recyclerViewDonMua);
        referenceHD = FirebaseDatabase.getInstance().getReference().child("hoadon");
        DataFromFirebaseListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewDonHang(getActivity() , listSanPhamDonHang);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void DataFromFirebaseListener(){
        referenceHD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("MTP", "DataFromFirebaseListener: 3");
                    String key = ds.child("id").getValue(String.class);
                    String idUser = ds.child("idUser").getValue(String.class);
                    String ngayTaoDon = ds.child("ngaytaodon").getValue(String.class);
                    String tenUser = ds.child("hoten").getValue(String.class);
                    String sodienthoai = ds.child("sodienthoai").getValue(String.class);
                    int tongtien = ds.child("tongtien").getValue(Integer.class);
                    String trangthai = ds.child("trangthai").getValue(String.class);
                    String diachi = ds.child("diachi").getValue(String.class);
                    int tongtienGoc = ds.child("tongtienGoc").getValue(Integer.class);
                    AtomicBoolean isTaiKhoan = new AtomicBoolean();
                    if(idUser.equals(MainActivity.id)){
                        isTaiKhoan.set(true);
                    }
                    if(isTaiKhoan.get() == true){
                       HoaDon hd = new HoaDon(key,tongtien,ngayTaoDon,"",tenUser,sodienthoai,diachi,trangthai,idUser,"",tongtienGoc);
                        listSanPhamDonHang.add(hd);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
