package com.example.appbanhang;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appbanhang.models.LichSuTruyCap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentDaDangNhap extends Fragment {
    TextView txthoten,txtsodienthoai,txtXemTT,txtthongtinlienhe;
    RelativeLayout rlttcn;
    Button btnDangXuat;
    LichSuTruyCap lichSuTruyCap;
    DatabaseReference reference;
    public static final  String SHARED_PREFS = "sharedPrefs";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentdadangnhap,container,false);
        rlttcn = view.findViewById(R.id.rlttcn);
        txthoten = view.findViewById(R.id.txthoten);
        txtsodienthoai = view.findViewById(R.id.txtsodienthoai);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        txtXemTT = view.findViewById(R.id.txtXemTT);
        txtthongtinlienhe = view.findViewById(R.id.thongtinlienhe);
        txthoten.setText(MainActivity.hoten);
        txtsodienthoai.setText(MainActivity.sodienthoai);
        reference = FirebaseDatabase.getInstance().getReference().child("lichsutruycap");
        rlttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FormTTCN.class);
                startActivity(intent);
            }
        });
        txtXemTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),donMua.class);
                startActivity(intent);
            }
        });
        txtthongtinlienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TTLH.class);
                startActivity(intent);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String keyLSTC = reference.push().getKey();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String ngaygio = sdf.format(calendar.getTime());
                lichSuTruyCap = new LichSuTruyCap(MainActivity.hoten,MainActivity.sodienthoai,ngaygio,"Đăng Xuất");
                reference.child(keyLSTC).setValue(lichSuTruyCap);
                removeSharedPreferences();
                restartApp();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void restartApp() {
        Intent intent = new Intent(this.getContext(), WellComeApp.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.getContext(), 1000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
    private void  removeSharedPreferences(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
