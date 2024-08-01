 package com.example.appbanhang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appbanhang.models.ChiTietHoaDon;
import com.example.appbanhang.models.HoaDon;
import com.example.appbanhang.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class giohangthongtin extends Fragment {
    EditText edthoten,edtsodienthoai,edtdiachi;
    Button btnHoanTat;
    ChiTietHoaDon chiTietHoaDon;
    DatabaseReference referenceSP;
    DatabaseReference referenceHD;
    DatabaseReference referenceCTHD;
    ArrayList<SanPham> listSP = new ArrayList<>();
    final Calendar myCalendar= Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final  String SHARED_PREFS = "sharedPrefs";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.giohangthongtin,container,false);
        edthoten = view.findViewById(R.id.edthoten);
        edtsodienthoai = view.findViewById(R.id.edtsodienthoai);
        edtdiachi = view.findViewById(R.id.edtDiaChi);
        btnHoanTat = view.findViewById(R.id.btnHoanTat);
        referenceSP = FirebaseDatabase.getInstance().getReference().child("sanpham");
        referenceHD = FirebaseDatabase.getInstance().getReference().child("hoadon");
        referenceCTHD = FirebaseDatabase.getInstance().getReference().child("chitiethoadon");
        edthoten.setText(MainActivity.hoten);
        edtsodienthoai.setText(MainActivity.sodienthoai);
        edtdiachi.setText(MainActivity.diachi);

        String hoten = edthoten.getText().toString();
        String sodienthoai = edtsodienthoai.getText().toString();
        String diachi = edtdiachi.getText().toString();
        readData();
        btnHoanTat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!validateHoTen() | !validateSoDienThoai() | !validateDiaChi()) {
                    return;
                }else {
                    String keyHD = referenceHD.push().getKey();
                    AtomicBoolean isCheck = new AtomicBoolean();
                    listSP.forEach(sanPham -> {
                        MainActivity.listGH.forEach(sp -> {
                            if(sanPham.getIdSP().equals(sp.getIdSP())){
                                int soluong = sanPham.getSoluongKho() - sp.getSoluong();
                                if(soluong >= 0){
                                    String keyCTHD = referenceCTHD.push().getKey();
                                    chiTietHoaDon = new ChiTietHoaDon(keyCTHD,sp.getIdSP(),keyHD,sp.getSoluong());
                                    referenceCTHD.child(keyCTHD).setValue(chiTietHoaDon);
                                    referenceSP.child(sp.getIdSP()).child("soluongKho").setValue(soluong);
                                }else {
                                    isCheck.set(true);
                                }
                            }
                        });
                    });
                    if(!isCheck.get()){
                        myCalendar.add(Calendar.DATE,2);
                        String ngaydukien = sdf.format(myCalendar.getTime());
                        String ngayhoanthanh = "";
                        String trangthai = "Chờ Xác Nhận";
                        HoaDon hoaDon = new HoaDon(keyHD,gioHangTinhTien.TT,ngaydukien,ngayhoanthanh,hoten,sodienthoai,diachi,trangthai,MainActivity.id,"",gioHangTinhTien.laisuat);
                        referenceHD.child(keyHD).setValue(hoaDon);
                        Toast.makeText(view.getContext(), "Tạo đơn hàng thành công", Toast.LENGTH_LONG).show();
                        MainActivity.listGH.clear();
                        saveData();
                        Intent intent = new Intent(getActivity(),donMua.class);
                        startActivity(intent);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Một số sản phẩm không đủ số lượng.Vui lòng chọn sản phẩm mới vào giỏ hàng.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.listGH.clear();
                                saveData();
                            }
                        });
                        builder.show();
                    }
                    }
                }
        });
        return view;
    }
    private void readData() {
        referenceSP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    int soluongKho = ds.child("soluongKho").getValue(Integer.class);
                    SanPham sp = new SanPham(key,"","",0,"","",soluongKho,0);
                    listSP.add(sp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private Boolean validateDiaChi (){
        String val = edtdiachi.getText().toString();
        if(val.isEmpty()){
            edtdiachi.setError("Vui lòng nhập địa chỉ");
            return false;
        }else {
            edtdiachi.setError(null);
            return true;
        }
    }
    private Boolean validateSoDienThoai (){
        String val = edtsodienthoai.getText().toString();

        if(val.isEmpty()){
            edtsodienthoai.setError("Vui lòng nhập số điện thoại");
            return false;
        }else {
            edtsodienthoai.setError(null);
            return true;
        }
    }
    private Boolean validateHoTen (){
        String val = edthoten.getText().toString();

        if(val.isEmpty()){
            edthoten.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }else {
            edthoten.setError(null);
            return true;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // creating a new variable for gson.
        Gson gson = new Gson();
        // getting data from gson and storing it in a string.
        String json = gson.toJson(MainActivity.listGH);
        //below line is to save data in shared
        //prefs in the form of string.
        editor.putString("listGH", json);
        editor.apply();
    }
}
