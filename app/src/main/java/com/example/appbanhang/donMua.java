package com.example.appbanhang;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class donMua extends AppCompatActivity {
    donMuaViewPaperAdapter donMuaAdapter;
    ImageButton imBack;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donmua);
        tabLayout = findViewById(R.id.tablayout);
        imBack = findViewById(R.id.imgb);
        viewPager = findViewById(R.id.viewpaper);
        Intent intent = getIntent();
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        donMuaAdapter = new donMuaViewPaperAdapter(getSupportFragmentManager());
        donMuaAdapter.addFragment(new FragmentChoXacNhan(),"Chờ Xác Nhận");
        donMuaAdapter.addFragment(new FragmentDangGiao(),"Đang giao");
        donMuaAdapter.addFragment(new FragmentDaGiao(),"Đã giao");
        donMuaAdapter.addFragment(new FragmentDaHuy(),"Đã hủy");
        viewPager.setAdapter(donMuaAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
