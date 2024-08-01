package com.example.appbanhang;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.models.LichSuTruyCap;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class FragmentDangNhap extends Fragment {
    EditText edtsodienthoai,edtmatkhau;
    Button btnDangNhap;
    ImageButton imbPassword;
    public static final  String SHARED_PREFS = "sharedPrefs";
    DatabaseReference referenceLSTC;
    LichSuTruyCap lichSuTruyCap;
    TextView txtquenmk;
    private final AtomicBoolean isCheckPass = new AtomicBoolean();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dang_nhap,container,false);
        edtsodienthoai = view.findViewById(R.id.edtsodienthoai);
        edtmatkhau = view.findViewById(R.id.edtmatkhau);
        btnDangNhap = view.findViewById(R.id.btnDangNhap);
        imbPassword = view.findViewById(R.id.imbPassword);
        txtquenmk = view.findViewById(R.id.txtquenmk);
        imbPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCheckPass.get()){
                    imbPassword.setImageResource(R.drawable.show_password);
                    edtmatkhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isCheckPass.set(true);
                }else {
                    imbPassword.setImageResource(R.drawable.hide_password);
                    edtmatkhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isCheckPass.set(false);
                }
            }
        });
        txtquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Quen_MK.class);
                startActivity(intent);
            }
        });
        referenceLSTC = FirebaseDatabase.getInstance().getReference().child("lichsutruycap");
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateTenDangNhap() | !validateMatKhau()){
                    return;
                }else {
                    isUser();
                }
            }
        });
        return view;
    }

    private Boolean validateTenDangNhap (){
        String val = edtsodienthoai.getText().toString();

        if(val.isEmpty()){
            edtsodienthoai.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        else if(val.length() != 10){
            edtsodienthoai.setError("Vui lòng nhập số điện thoại đủ 10 số");
            return false;
        }
        else {
            edtsodienthoai.setError(null);
            return true;
        }
    }
    private Boolean validateMatKhau (){
        String val = edtmatkhau.getText().toString();
        if(val.isEmpty()){
            edtmatkhau.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        else if(val.length() > 15 ){
            edtmatkhau.setError("Vui lòng nhập số mật khẩu ngắn hơn 15 kí tự");
            return false;
        }
        else {
            edtmatkhau.setError(null);
            return true;
        }
    }
    public  void isUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("taikhoan");
        String phone = edtsodienthoai.getText().toString().trim();
        String password = edtmatkhau.getText().toString().trim();
        Query checkTenDangNhap = reference.orderByChild("sodienthoai").equalTo(phone);
        checkTenDangNhap.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String key = ds.getKey();
                                String passwordFromDB = snapshot.child(key).child("matkhau").getValue(String.class);
                                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), passwordFromDB);
                                if (result.verified) {
                                    String hotenFromDB = snapshot.child(key).child("hoten").getValue(String.class);
                                    String sodienthoaiFromDB = snapshot.child(key).child("sodienthoai").getValue(String.class);
                                    String id = snapshot.child(key).child("id").getValue(String.class);
                                    String diachiFromDB = snapshot.child(key).child("diachi").getValue(String.class);
                                    String ngaysinhFromDB = snapshot.child(key).child("ngaysinh").getValue(String.class);
                                    String gioitinhFromDB = snapshot.child(key).child("gioitinh").getValue(String.class);
                                    String tenLoaiFromDB = snapshot.child(key).child("tenLoai").getValue(String.class);
                                    String ngaythamgiaFromDB = snapshot.child(key).child("ngaythamgia").getValue(String.class);
                                    boolean hoatdong = snapshot.child(key).child("hoatdong").getValue(Boolean.class);
                                    if (hoatdong) {
                                        MainActivity.id = id;
                                        MainActivity.dadangnhap = true;
                                        MainActivity.hoten = hotenFromDB;
                                        MainActivity.sodienthoai = sodienthoaiFromDB;
                                        MainActivity.diachi = diachiFromDB;
                                        MainActivity.ngaysinh = ngaysinhFromDB;
                                        MainActivity.gioitinh = gioitinhFromDB;
                                        MainActivity.tenLoai = tenLoaiFromDB;
                                        MainActivity.ngaythamgia = ngaythamgiaFromDB;
                                        saveData();
                                        String keyLSTC = referenceLSTC.push().getKey();
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                        String ngaygio = sdf.format(calendar.getTime());
                                        if (MainActivity.tenLoai.equals("client")) {
                                            lichSuTruyCap = new LichSuTruyCap(hotenFromDB, sodienthoaiFromDB, ngaygio, "Đăng Nhập");
                                            referenceLSTC.child(keyLSTC).setValue(lichSuTruyCap);
                                        }
                                        Toast.makeText(getContext(), "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                    } else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Thông báo");
                                        alertDialog.setMessage("Tài khoản hiện đã tạm khóa.");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                } else {
                                    edtmatkhau.setError("Sai mật khẩu");
                                    edtmatkhau.requestFocus();
                                }
                            }
                }else {
                        edtsodienthoai.setError("Tài khoản không tồn tại");
                        edtsodienthoai.requestFocus();
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void saveData(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hoten",MainActivity.hoten);
        editor.putString("sodienthoai",MainActivity.sodienthoai);
        editor.putString("diachi",MainActivity.diachi);
        editor.putString("ngaysinh",MainActivity.ngaysinh);
        editor.putString("gioitinh",MainActivity.gioitinh);
        editor.putString("ngaythamgia",MainActivity.ngaythamgia);
        editor.putString("tenloai",MainActivity.tenLoai);
        editor.putBoolean("daDangNhap",MainActivity.dadangnhap);
        editor.putString("id",MainActivity.id);
        editor.putBoolean("dadangnhap",MainActivity.dadangnhap);
//        // creating a new variable for gson.
//        Gson gson = new Gson();
//        // getting data from gson and storing it in a string.
//        String json = gson.toJson(MainActivity.listGH);
//        String json2 = gson.toJson(MainActivity.listYT);
//        // below line is to save data in shared
//        // prefs in the form of string.
//        editor.putString("listGH", json);
//        editor.putString("listYT", json2);
        editor.apply();
    }
}
