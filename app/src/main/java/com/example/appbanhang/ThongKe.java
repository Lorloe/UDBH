package com.example.appbanhang;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.models.HoaDon;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThongKe extends AppCompatActivity {
    BarChart barChart;
    DatabaseReference reference;
    private int tongtien;
    TextView txtDSHN,txtDS7N,txtDS30N,txtLNHN,txtLN7N,txtLN30N;
    private int tongtienDSHN,tongtienDS7N,tongtienDS30N,tongtienLNHN,tongtienLN7N,tongtienLN30N;
    private Locale localeEN = new Locale("en", "EN");
    private NumberFormat en = NumberFormat.getInstance(localeEN);
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke);
        anhxa();
        readData();
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
    private void readData(){
        ArrayList<HoaDon> list = new ArrayList<>();
        ArrayList<BarEntry> visitors = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("hoadon");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        int tongtien = ds.child("tongtien").getValue(Integer.class);
                        int laisuat = ds.child("laisuat").getValue(Integer.class);
                        String ngayhoanthanh = ds.child("ngayhoanthanh").getValue(String.class);
                        HoaDon hoaDon = new HoaDon("", tongtien, "", ngayhoanthanh, "", "", "", "", "","",laisuat);
                        list.add(hoaDon);
                    }
                    if (list.size() > 0) {
                        int count = 0;
                        final LocalDate date = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String formattedDate = date.format(formatter);
                        list.forEach(hoaDon -> {
                            if(hoaDon.getNgayhoanthanh().equals(formattedDate.trim())){
                                tongtienDSHN += hoaDon.getTongtien();
                                tongtienLNHN += hoaDon.getLaisuat();
                            }
                        });
                        for (int i = 6; i >=0; i--) {
                            AtomicBoolean isCheckDate = new AtomicBoolean();
                            final LocalDate dateMinus7Days = date.minusDays(i+1);
                            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM");
                            String formattedDate1 = dateMinus7Days.format(formatter1);
                            String formattedDate2 = dateMinus7Days.format(formatter2);
                            list.forEach(hoaDon -> {
                                if (hoaDon.getNgayhoanthanh().equals(formattedDate1.trim())) {
                                    tongtien += hoaDon.getTongtien();
                                    tongtienDS7N += hoaDon.getTongtien();
                                    tongtienLN7N += hoaDon.getLaisuat();
                                    isCheckDate.set(true);
                                }
                            });
                            if(isCheckDate.get()){
                                visitors.add(new BarEntry(count, tongtien));
                                count++;
                            }else {
                                visitors.add(new BarEntry(count, 0));
                                count++;
                            }
                            tongtien = 0;
                            BarDataSet barDataSet = new BarDataSet(visitors, "Doanh Thu");
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);

                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);

                            days.add(formattedDate2);

                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
                            xAxis.setCenterAxisLabels(false);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1);
                            xAxis.setGranularityEnabled(true);

                            barData.setBarWidth(0.1f);

                            barChart.setFitBars(true);
                            barChart.getDescription().setText("");
                            barChart.animateY(2000);
                        }
                        String str = en.format(tongtienDS7N);
                        txtDS7N.setText(str);
                        String str1 = en.format(tongtienLN7N);
                        txtLN7N.setText(str1);
                        for(int i = 0; i < 31; i++){
                            final LocalDate dateMinus = date.minusDays(i+1);
                            DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String formattedDate3 = dateMinus.format(formatter3);
                            list.forEach(hoaDon -> {
                                if (hoaDon.getNgayhoanthanh().equals(formattedDate3.trim())) {
                                    tongtienDS30N += hoaDon.getTongtien();
                                    tongtienLN30N += hoaDon.getLaisuat();
                                }
                            });
                        }
                        String str2 = en.format(tongtienDS30N);
                        txtDS30N.setText(str2);
                        String str3 = en.format(tongtienLN30N);
                        txtLN30N.setText(str3);

                    }
                    String str = en.format(tongtienDSHN);
                    txtDSHN.setText(str);
                    String str1 = en.format(tongtienLNHN);
                    txtLNHN.setText(str1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void tinhTongTien(){
        ArrayList<HoaDon> list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("hoadon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void anhxa(){
        barChart = findViewById(R.id.barChart);
        txtDSHN = findViewById(R.id.txtDSHN);
        txtDS7N = findViewById(R.id.txtDS7N);
        txtDS30N = findViewById(R.id.txtDS30N);
        txtLNHN = findViewById(R.id.txtLNHN);
        txtLN7N = findViewById(R.id.txtLN7N);
        txtLN30N = findViewById(R.id.txtLN30N);
    }
}
