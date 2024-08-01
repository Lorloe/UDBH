package com.example.appbanhang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appbanhang.models.SanPham;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class gioHangTinhTien extends Fragment {
    Button btnNhapThongTin;
    ListView lvgh;
    TextView txttongtien;
    private int tongtien = 0;
    private int tongtienGoc = 0;
    public static int TT;
    public static int laisuat;
    gioHangAdapter gioHangAdapter;
    public gioHangTinhTien(){
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.giohangtinhtien, container, false);
        btnNhapThongTin = view.findViewById(R.id.btnNhapThongTin);
        lvgh = (ListView) view.findViewById(R.id.listGioHang);
        txttongtien = (TextView) view.findViewById(R.id.txttongtien);
        gioHangAdapter = new gioHangAdapter(getActivity(), MainActivity.listGH, txttongtien);
        lvgh.setAdapter(gioHangAdapter);
        gioHangAdapter.notifyDataSetChanged();
        btnNhapThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.dadangnhap&& MainActivity.listGH.size() == 0) {
                    Toast.makeText(getActivity(), "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                } else if(MainActivity.dadangnhap && MainActivity.listGH.size() > 0){
                    giohangthongtin fragment = new giohangthongtin();
                    FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayoutGioHang, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    GioHangPage.txtChecked.setBackgroundResource(R.drawable.circle);
                    GioHangPage.txtUnchecked.setBackgroundResource(R.drawable.circle);
                }else {
                    Intent intent = new Intent(getActivity(), FormDNDK.class);
                    startActivity(intent);
                }

            }
        });
        return view;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        GioHangPage.txtChecked.setBackgroundResource(R.drawable.circle);
        GioHangPage.txtUnchecked.setBackgroundResource(R.drawable.circle2);
        gioHangAdapter.notifyDataSetChanged();
        MainActivity.listGH.forEach(sanPham -> {
            tongtien += (sanPham.getSoluong()*sanPham.getGiaSP());
            tongtienGoc += (sanPham.getSoluong()*sanPham.getGiaGoc());
            txttongtien.setText("Giá: " + tongtien +" VNĐ");
            TT = tongtien;
            laisuat = TT - tongtienGoc;
        });
        Log.d("gia", "onResume: "+laisuat);
        tongtien = 0;
        tongtienGoc = 0;
    }
}
