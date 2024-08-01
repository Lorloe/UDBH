package com.example.appbanhang;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SanPhamPage extends AppCompatActivity { ;
    GridView gridView;
    TextView txttensp;
    ImageView iconThuongHieu;
    DatabaseReference reference;
    public ArrayList<SanPham> list = new ArrayList<SanPham>();
    sanPhamAdapter adapter;
    String thuongHieuID;
    ImageButton imageBack;
    String thuongHieuHinh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sanpham);
        iconThuongHieu = findViewById(R.id.iconThuongHieu);
        imageBack = (ImageButton) findViewById(R.id.imgBack);
        Bundle bundle = getIntent().getExtras();
        thuongHieuID = bundle.getString("id");
        thuongHieuHinh = bundle.getString("hinh");
        gridView = findViewById(R.id.grid_view_sanpham);
        Uri myUri = Uri.parse(thuongHieuHinh);
        Picasso.with(SanPhamPage.this).load(myUri).placeholder(R.drawable.check_icon).into(iconThuongHieu);
        txttensp = findViewById(R.id.txttensp);
        reference = FirebaseDatabase.getInstance().getReference().child("sanpham");
        DataFromFirebaseListener();
        adapter = new sanPhamAdapter(SanPhamPage.this,list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "onItemClick: ");
                reference = FirebaseDatabase.getInstance().getReference().child("sanpham");
                SanPham sanPhamItem = list.get(position);
                Intent intent = new Intent(SanPhamPage.this, chiTietSanPham.class);
                intent.putExtra("id", sanPhamItem.getIdSP());
                intent.putExtra("ten", sanPhamItem.getTenSP());
                intent.putExtra("hinh", sanPhamItem.getHinhSP());
                intent.putExtra("gia", sanPhamItem.getGiaSP());
                intent.putExtra("mota", sanPhamItem.getMotaSP());
                intent.putExtra("idTH",sanPhamItem.getIdTH());
                intent.putExtra("soluongKho",sanPhamItem.getSoluongKho());
                intent.putExtra("giaGoc",sanPhamItem.getGiaGoc());
                startActivity(intent);
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void DataFromFirebaseListener() {
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String key = ds.getKey();
                    String hinhsp = ds.child("hinhSP").getValue(String.class);
                    String tensp = ds.child("tenSP").getValue(String.class);
                    int giasp = ds.child("giaSP").getValue(Integer.class);
                    String motasp = ds.child("motaSP").getValue(String.class);
                    String idTH = ds.child("idTH").getValue(String.class);
                    int soluongKho = ds.child("soluongKho").getValue(Integer.class);
                    int giaGoc = ds.child("giaGoc").getValue(Integer.class);
                    AtomicBoolean isDaTonTai = new AtomicBoolean(false);
                    AtomicBoolean isYeuThich = new AtomicBoolean(false);
                    AtomicBoolean isThuongHieu = new AtomicBoolean(false);

                    /// check san pham da ton tai hay chua ?
                    list.forEach(sanpham -> {
                        /// so sánh id thương hiệu (idthuong hieu la khóa ngoại) để lấy ra được danh sách theo thương hiệu
                        /// check key de khong bi trung
                        if (sanpham.getIdSP().equals(key)) {
                            isDaTonTai.set(true);
                        }
                    });
                    /// check thương hiệu click từ homepage
                    if(thuongHieuID.equals(idTH)){
                        isThuongHieu.set(true);
                    }

                    /// danh sach yeu thich
                    MainActivity.listYT.forEach(sanPhamYT -> {
                        if (key.equals(sanPhamYT.getIdSP())) {
                            isYeuThich.set(true);
                        }
                    });

                    /// nó phải chưa được tồn tại
                    /// nó phải cùng thương hiệu với homepage khi click vào
                    if (!isDaTonTai.get() && isThuongHieu.get()) {
                        /// -> them vao
                        SanPham sp = new SanPham(key, tensp, hinhsp, giasp, motasp, idTH,soluongKho,giaGoc);
                        list.add(sp);
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
