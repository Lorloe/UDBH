package com.example.appbanhang;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appbanhang.models.HoaDon;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class QLDH extends AppCompatActivity {
    FragmentPager_AdminQLDH adapter;
    ImageButton imBack;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_qldh);
        tabLayout = findViewById(R.id.tablayout);
        imBack = findViewById(R.id.imgb);
        viewPager = findViewById(R.id.viewpaper);
        adapter = new FragmentPager_AdminQLDH(getSupportFragmentManager());
        adapter.addFragment(new Fragment_AdminChoXacNhan(), "Chờ Xác Nhận");
        adapter.addFragment(new Fragment_AdminDangGiao(), "Đang giao");
        adapter.addFragment(new Fragment_AdminDaGiao(), "Đã giao");
        adapter.addFragment(new Fragment_AdminDaHuy(), "Đã giao");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
