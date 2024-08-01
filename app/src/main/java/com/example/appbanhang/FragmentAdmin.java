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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class FragmentAdmin  extends Fragment {
    @Nullable
    RelativeLayout rlttcn;
    TextView txtQLDH,txtQLTK,txtQLDT,txtCNSP,txthoten,txtsodienthoai;
    Button btnDangXuat;
    public static final  String SHARED_PREFS = "sharedPrefs";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentadmin,container,false);
        rlttcn = view.findViewById(R.id.rlttcn);
        txtQLDH = view.findViewById(R.id.txtQLDH);
        txtQLTK = view.findViewById(R.id.txtQLTK);
        txtCNSP = view.findViewById(R.id.txtCNSP);
        txtQLDT = view.findViewById(R.id.txtQLDT);
        txthoten = view.findViewById(R.id.txthoten);
        txtsodienthoai = view.findViewById(R.id.txtsodienthoai);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        txthoten.setText(MainActivity.hoten);
        txtsodienthoai.setText(MainActivity.sodienthoai);
        rlttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FormTTCN.class);
                startActivity(intent);
            }
        });
        txtQLTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLND.class);
                startActivity(intent);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                removeSharedPreferences();
                restartApp();
            }
        });
        txtQLDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLDH.class);
                startActivity(intent);
            }
        });
        txtCNSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_capnhat.class);
                startActivity(intent);
            }
        });
        txtQLDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ThongKe.class);
                startActivity(intent);
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
