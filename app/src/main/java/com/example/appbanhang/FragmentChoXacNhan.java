package com.example.appbanhang;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.HoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FragmentChoXacNhan extends Fragment {
    public ArrayList<HoaDon> listCXN;
    RecyclerView recyclerView;
    DatabaseReference reference;
    RecyclerViewDonHang adapter;
    ImageView image_DHT;
    TextView txtDonHangTrong;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentchoxacnhan,container,false);
        recyclerView = view.findViewById(R.id.listChoXacNhan);
        image_DHT = view.findViewById(R.id.image_DHT);
        txtDonHangTrong = view.findViewById(R.id.txtDonHangTrong);
        return view;
    }
    private void DataFromFirebaseListener() {
        listCXN = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("hoadon");
        Query query = reference.orderByChild("trangthai").equalTo("Chờ Xác Nhận");
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.child("id").getValue(String.class);
                    String idUser = ds.child("idUser").getValue(String.class);
                    String ngaydukien = ds.child("ngaydukien").getValue(String.class);
                    String tenUser = ds.child("hoten").getValue(String.class);
                    String sodienthoai = ds.child("sodienthoai").getValue(String.class);
                    int tongtien = ds.child("tongtien").getValue(Integer.class);
                    String trangthai = ds.child("trangthai").getValue(String.class);
                    String diachi = ds.child("diachi").getValue(String.class);
                    int laisuat = ds.child("laisuat").getValue(Integer.class);
                    AtomicBoolean isTaiKhoan = new AtomicBoolean();
                    AtomicBoolean isDatontai = new AtomicBoolean();
                    listCXN.forEach(hoaDon -> {
                        if(hoaDon.getId().equals(key)){
                            isDatontai.set(true);
                        }
                    });
                    if(idUser.equals(MainActivity.id)){
                        isTaiKhoan.set(true);
                    }
                    if(isTaiKhoan.get() && !isDatontai.get()){
                        HoaDon hd = new HoaDon(key,tongtien,ngaydukien,"",tenUser,sodienthoai,diachi,trangthai,idUser,"",laisuat);
                        listCXN.add(hd);
                    }
                }
                adapter.notifyDataSetChanged();
                if(listCXN.size() == 0){
                    image_DHT.setVisibility(View.VISIBLE);
                    txtDonHangTrong.setVisibility(View.VISIBLE);
                }else {
                    image_DHT.setVisibility(View.GONE);
                    txtDonHangTrong.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DataFromFirebaseListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewDonHang(getActivity() , listCXN);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
