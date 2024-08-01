package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.appbanhang.models.HoaDon;
import com.example.appbanhang.models.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    public static  ArrayList<SanPham> listGH = new ArrayList<>();
    public static  ArrayList<SanPham> listYT = new ArrayList<>();
    public static String id;
    public static Boolean dadangnhap = false;
    public static String hoten;
    public static String sodienthoai;
    public static String diachi;
    public static String ngaysinh;
    public static String gioitinh;
    public static String ngaythamgia;
    public static String tenLoai;
    SharedPreferences sharedPreferences;
    public static final  String SHARED_PREFS = "sharedPrefs";
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpapermain);
        setUpViewpaper();
        loadData();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.ic_favorite:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.ic_giohang:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.ic_me:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
    private void setUpViewpaper() {
        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPaperAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.ic_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.ic_favorite).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.ic_giohang).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.ic_me).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.aqua));
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    public void loadData(){
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        id = sharedPreferences.getString("id",null);
         dadangnhap = sharedPreferences.getBoolean("dadangnhap",false);
        hoten = sharedPreferences.getString("hoten",null);
         sodienthoai = sharedPreferences.getString("sodienthoai",null);
        diachi = sharedPreferences.getString("diachi",null);
        ngaysinh = sharedPreferences.getString("ngaysinh",null);
        gioitinh = sharedPreferences.getString("gioitinh",null);
        ngaythamgia = sharedPreferences.getString("ngaythamgia",null);
        tenLoai = sharedPreferences.getString("tenloai",null);
        Gson gson = new Gson();
        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("listGH", null);
        String json2 = sharedPreferences.getString("listYT", null);
        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<SanPham>>() {}.getType();
        listGH = gson.fromJson(json, type);
        listYT = gson.fromJson(json2, type);
        // checking below if the array list is empty or not
        if (listGH == null) {
            // if the array list is empty
            // creating a new array list.
            listGH = new ArrayList<>();
        }
        if (listYT == null) {
            // if the array list is empty
            // creating a new array list.
            listYT = new ArrayList<>();
        }
    }
}