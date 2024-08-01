package com.example.appbanhang;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.ChiTietHoaDon;
import com.example.appbanhang.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CTHoaDon extends AppCompatActivity {
    ImageButton imgBack;
    TextView txtTenNhanHang,txtSoDienThoaiNhanHang,txtDiaChiNhanHang,txtlido;
    RecyclerView recyclerChiTietDonHang;
    Button btnHuy;
    DatabaseReference referenceCTHD,referenceSP;
    Recycler_ChiTietDonHang adapter;
    ArrayList<SanPham> listSP;
    ArrayList<ChiTietHoaDon> listCTDH;
    String idHD;
    int tongtien;
    String trangthai,lido;
    RadioButton radioButton;
    DatabaseReference referenceHD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chitietdonhang);
        anhxa();
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
            }
        });
        Intent intent = getIntent();
        idHD = intent.getStringExtra("idHD");
        tongtien = intent.getIntExtra("tongtien",0);
        txtTenNhanHang.setText(intent.getStringExtra("hoten"));
        txtSoDienThoaiNhanHang.setText(intent.getStringExtra("sodienthoai"));
        txtDiaChiNhanHang.setText(intent.getStringExtra("diachi"));
        trangthai = intent.getStringExtra("trangthai");
        lido = intent.getStringExtra("lido");
        if(!trangthai.equals("Chờ Xác Nhận")){
            btnHuy.setVisibility(View.GONE);
        }
        if(trangthai.equals("Đã Hủy")){
            txtlido.setVisibility(View.VISIBLE);
            txtlido.setText("Lí do hủy đơn hàng: "+lido);
        }
        referenceCTHD = FirebaseDatabase.getInstance().getReference().child("chitiethoadon");
        referenceSP = FirebaseDatabase.getInstance().getReference().child("sanpham");
        DataFromFirebaseListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CTHoaDon.this);
        recyclerChiTietDonHang.setLayoutManager(linearLayoutManager);
        adapter = new Recycler_ChiTietDonHang(CTHoaDon.this , listSP,listCTDH);
        recyclerChiTietDonHang.setAdapter(adapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void DataFromFirebaseListener() {
        listSP = new ArrayList<>();
        listCTDH = new ArrayList<>();
        referenceCTHD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String keyCT = ds.getKey();
                    String idSP = ds.child("idSP").getValue(String.class);
                    String idHDFromDatabase = ds.child("idHD").getValue(String.class);
                    int soluong = ds.child("soluong").getValue(Integer.class);
                    AtomicBoolean isHoaDon = new AtomicBoolean();
                    if(idHD.equals(idHDFromDatabase)){
                        isHoaDon.set(true);
                    }
                    referenceSP.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String key = ds.getKey();
                                String hinhsp = ds.child("hinhSP").getValue(String.class);
                                String tensp = ds.child("tenSP").getValue(String.class);
                                int giasp = ds.child("giaSP").getValue(Integer.class);
                                String idTH = ds.child("idTH").getValue(String.class);
                                String motaSP = ds.child("motaSP").getValue(String.class);
                                int soluongKho = ds.child("soluongKho").getValue(Integer.class);
                                int giaGoc = ds.child("giaGoc").getValue(Integer.class);
                                AtomicBoolean isSanPham = new AtomicBoolean();
                                assert key != null;
                                if(key.equals(idSP)){
                                    isSanPham.set(true);
                                }
                                if(isHoaDon.get() && isSanPham.get()){
                                    ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(keyCT,key,idHD,soluong);
                                    listCTDH.add(chiTietHoaDon);
                                    SanPham sanPham = new SanPham(key,tensp,hinhsp,soluong*giasp,motaSP,idTH,soluongKho,giaGoc);
                                    listSP.add(sanPham);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void anhxa(){
        imgBack = findViewById(R.id.imgb);
        txtTenNhanHang = findViewById(R.id.txtTenNhanHang);
        txtSoDienThoaiNhanHang = findViewById(R.id.txtSoDienThoaiNhanHang);
        txtDiaChiNhanHang = findViewById(R.id.txtDiaChiNhanHang);
        recyclerChiTietDonHang = findViewById(R.id.RecyclerChiTietDonHang);
        btnHuy = findViewById(R.id.btndahuyDH);
        txtlido = findViewById(R.id.txtlido);
    }
    private void openDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_huydh);

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

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
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
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = dialog.findViewById(selectedId);
                    String trangthai = "Đã Hủy";
                    referenceHD = FirebaseDatabase.getInstance().getReference().child("hoadon");
                    Query query = referenceHD.orderByChild("id").equalTo(idHD);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                referenceHD.child(idHD).child("trangthai").setValue(trangthai.trim());
                                referenceHD.child(idHD).child("ngaydukien").setValue("".trim());
                                referenceHD.child(idHD).child("lido").setValue(radioButton.getText().toString().trim());
                                Toast.makeText(CTHoaDon.this,"Đã hủy đơn hàng",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
