package com.example.appbanhang;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.models.LichSuTruyCap;
import com.example.appbanhang.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class FragmentDangKy extends Fragment {
    EditText edthoten, edtsodienthoai, edtmatkhau, edtdiachi, edtngaysinh;
    Button btnDangKy;
    CheckBox cbnam,cbnu;
    final Calendar myCalendar= Calendar.getInstance();
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference referenceLSTC;
    LichSuTruyCap lichSuTruyCap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dang_ky,container,false);
        edthoten = view.findViewById(R.id.edthoten);
        edtsodienthoai = view.findViewById(R.id.edtsodienthoai);
        edtmatkhau = view.findViewById(R.id.edtmatkhau);
        edtdiachi = view.findViewById(R.id.edtdiachi);
        edtngaysinh = view.findViewById(R.id.edtngaysinh);
        cbnam = view.findViewById(R.id.cbnam);
        cbnu = view.findViewById(R.id.cbnu);
        btnDangKy = view.findViewById(R.id.btnDangKy);
        referenceLSTC = FirebaseDatabase.getInstance().getReference().child("lichsutruycap");
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        edtngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        cbnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbnu.setChecked(false);
                }
            }
        });
        cbnu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbnam.setChecked(false);
                }
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateHoTen() | !validateSoDienThoai() | !validateMatKhau() |  !validateDiaChi() | !validateNgaySinh()){
                    return;
                }else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String ngaygio = sdf2.format(calendar.getTime());
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference().child("taikhoan");
                    Query checkSDT = reference.orderByChild("sodienthoai").equalTo(edtsodienthoai.getText().toString().trim());
                    checkSDT.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                edtsodienthoai.setError("Số điện thoại này đã được sử dụng");
                            }else {
                                String key = reference.push().getKey();
                                String hoten = edthoten.getText().toString().trim();
                                String sodienthoai = edtsodienthoai.getText().toString().trim();
                                String matkhau = edtmatkhau.getText().toString().trim();
                                String bcryptHashString = BCrypt.withDefaults().hashToString(12, matkhau.toCharArray());
                                String diachi = edtdiachi.getText().toString().trim();
                                String ngaysinh = edtngaysinh.getText().toString().trim();
                                String ngaythamgia = sdf1.format(calendar.getTime());
                                String tenLoai = "client";
                                String gioitinh = "";
                                if (cbnam.isChecked()) {
                                    gioitinh = cbnam.getText().toString();
                                } else if (cbnu.isChecked()) {
                                    gioitinh = cbnu.getText().toString();
                                }
                                User user = new User(key,hoten, sodienthoai, bcryptHashString, diachi, ngaysinh, gioitinh,tenLoai,ngaythamgia,true);
                                reference.child(key).setValue(user);
                                String keyLSTC = referenceLSTC.push().getKey();
                                lichSuTruyCap = new LichSuTruyCap(hoten, sodienthoai, ngaygio, "Tạo Tài Khoản");
                                referenceLSTC.child(keyLSTC).setValue(lichSuTruyCap);
                                Toast.makeText(view.getContext(), "Đăng Ký Thành Công", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return view;
    }
    private Boolean validateHoTen (){
        String val = edthoten.getText().toString();

        if(val.isEmpty()){
            edthoten.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }else if(val.length() > 40 ){
            edtmatkhau.setError("Vui lòng nhập số mật khẩu ngắn hơn 40 kí tự");
            return false;
        }
        else {
            edthoten.setError(null);
            return true;
        }
    }
    private Boolean validateSoDienThoai (){
        String val = edtsodienthoai.getText().toString();
        if(val.isEmpty()){
            edtsodienthoai.setError("Vui lòng nhập số điện thoại");
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
        }else if(val.length() > 15 ){
            edtmatkhau.setError("Vui lòng nhập số mật khẩu ngắn hơn 15 kí tự");
            return false;
        }
        else {
            edtmatkhau.setError(null);
            return true;
        }
    }
    private Boolean validateDiaChi (){
        String val = edtdiachi.getText().toString();

        if(val.isEmpty()){
            edtdiachi.setError("Vui lòng nhập địa chỉ");
            return false;
        }else if(val.length() > 30 ){
            edtmatkhau.setError("Vui lòng nhập số địa chỉ ngắn hơn 30 kí tự");
            return false;
        }
        else {
            edtdiachi.setError(null);
            return true;
        }
    }
    private Boolean validateNgaySinh(){
        String val = edtngaysinh.getText().toString();
        if(val.isEmpty()){
            edtngaysinh.setError("Vui lòng nhập ngày sinh");
            return false;
        }else {
            edtngaysinh.setError(null);
            return true;
        }
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtngaysinh.setText(sdf.format(myCalendar.getTime()));
    }
};
