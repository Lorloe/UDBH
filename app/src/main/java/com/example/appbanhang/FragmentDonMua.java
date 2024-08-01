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

public class FragmentDonMua extends Fragment {
    ChiTietHoaDon chiTietHoaDon;
    DatabaseReference referenceCTHD;
    DatabaseReference referenceSP;
    RecyclerView recyclerView;
    ArrayList<ChiTietHoaDon> chiTietHoaDons;
    public ArrayList<SanPhamDonMua> listSanPhamDonMua = new ArrayList<SanPhamDonMua>();
    public ArrayList<SanPham> listSP;
    private donMuaAdapter adapter;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tiendogiaohang,container,false);
        Intent intent = getActivity().getIntent();
        String idHD = intent.getStringExtra("idHD");
        recyclerView = view.findViewById(R.id.recyclerViewDonMua);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        referenceCTHD = FirebaseDatabase.getInstance().getReference().child("chitiethoadon");
        referenceSP = FirebaseDatabase.getInstance().getReference().child("sanpham");
        adapter = new donMuaAdapter(this.getActivity() , listSanPhamDonMua);
        recyclerView.setAdapter(adapter);
        DataFromFirebaseListener();
        return view;
    }
    private void DataFromFirebaseListener(){
        chiTietHoaDons = new ArrayList<>();

        listSP = new ArrayList<>();
        Log.d("MTL", "DataFromFirebaseListener: 0");

        referenceCTHD.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("MTL", "DataFromFirebaseListener: 3");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    String idSP = ds.child("idSP").getValue(String.class);
                    String idHD = ds.child("idHD").getValue(String.class);
                    int soluong = ds.child("soluong").getValue(Integer.class);
                    chiTietHoaDons.add(new ChiTietHoaDon(key,idSP,idHD,soluong));
                }
                referenceSP.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String idSPFb = ds.child("idSP").getValue(String.class);
                            String hinhsp = ds.child("hinhSP").getValue(String.class);
                            String tensp = ds.child("tenSP").getValue(String.class);
                            int giasp = ds.child("giaSP").getValue(Integer.class);
                            String motasp = ds.child("motaSP").getValue(String.class);
                            int soluongKho = ds.child("soluongKho").getValue(Integer.class);
                            int giaGoc = ds.child("giaGoc").getValue(Integer.class);
                            String idTH = ds.child("idTH").getValue(String.class);
                            String idSP = idSPFb;
                            SanPham sp = new SanPham(idSP, tensp, hinhsp, giasp, motasp, idTH,soluongKho,giaGoc);
                            listSP.add(sp);
                        }


                        for (ChiTietHoaDon itemChiTiet : chiTietHoaDons) {
                            for(SanPham itemSanPham : listSP){
                                if((itemChiTiet.getIdSP()).equals(itemSanPham.getIdSP())){
                                    String tenSP = itemSanPham.getTenSP();
                                    String hinhSP = itemSanPham.getHinhSP();
                                    int giaSP = itemSanPham.getGiaSP();
                                    int soluong = itemChiTiet.getSoluong();
                                    SanPhamDonMua sanPhamDonMua = new SanPhamDonMua(tenSP, hinhSP, giaSP, soluong);
                                    listSanPhamDonMua.add(sanPhamDonMua);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
