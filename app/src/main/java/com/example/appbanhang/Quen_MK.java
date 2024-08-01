package com.example.appbanhang;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Quen_MK extends AppCompatActivity {
    ImageButton imb;
    EditText edtsodienthoai,edtngaysinh;
    Button btnXacNhan;
    DatabaseReference reference;
    ArrayList<User> list;
    String idUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quenmk);
        imb = findViewById(R.id.imgb);
        edtsodienthoai = findViewById(R.id.edtsodienthoai);
        edtngaysinh = findViewById(R.id.edtngaysinh);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        checkUser();
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!validateSoDienThoai() || !validateNgaySinh()){
                    return;
                }else {
                    AtomicBoolean isCheckSDT = new AtomicBoolean();
                    AtomicBoolean isCheckNgaySinh = new AtomicBoolean();
                    list.forEach(user -> {
                        if(user.getSodienthoai().equals(edtsodienthoai.getText().toString().trim()) && !user.getNgaysinh().equals(edtngaysinh.getText().toString().trim())){
                            isCheckSDT.set(true);
                        }else if(user.getNgaysinh().equals(edtngaysinh.getText().toString().trim()) && !user.getSodienthoai().equals(edtsodienthoai.getText().toString().trim())){
                            isCheckNgaySinh.set(true);
                        }else if(user.getSodienthoai().equals(edtsodienthoai.getText().toString().trim()) && user.getNgaysinh().equals(edtngaysinh.getText().toString().trim())){
                            idUser = user.getId();
                            isCheckSDT.set(true);
                            isCheckNgaySinh.set(true);
                        }
                    });
                    if(!isCheckSDT.get()){
                        edtsodienthoai.setError("Số điện thoại không tồn tại");
                    }else if(!isCheckNgaySinh.get()){
                        edtngaysinh.setError("Ngày sinh không đúng");
                    }else {
                        openDialog(Gravity.CENTER);
                    }
                }
            }
        });
    }
    private void checkUser(){
        list= new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("taikhoan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String key = ds.getKey();
                    String ngaysinh = ds.child("ngaysinh").getValue(String.class);
                    String sodienthoai = ds.child("sodienthoai").getValue(String.class);
                    User user = new User(key,"",sodienthoai,"","",ngaysinh,"","","",false);
                    list.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    private void openDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_matkhaumoi);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        EditText edtmatkhaumoi = dialog.findViewById(R.id.edtmatkhaumoi);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtmatkhaumoi.getText().toString().trim().isEmpty() || edtmatkhaumoi.getText().toString().length() > 15){
                    edtmatkhaumoi.setError("Vui lòng nhập mật khẩu mới nhỏ hơn 15 kí tự");
                }else{
                    String bcryptHashString = BCrypt.withDefaults().hashToString(12, edtmatkhaumoi.getText().toString().trim().toCharArray());
                    reference.child(idUser).child("matkhau").setValue(bcryptHashString);
                    Toast.makeText(Quen_MK.this,"Mật khẩu đã được thay đổi",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.aqua));
        }
    }
}
