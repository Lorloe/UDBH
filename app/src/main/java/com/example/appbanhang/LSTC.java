package com.example.appbanhang;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.models.LichSuTruyCap;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LSTC extends AppCompatActivity {
    DatabaseReference reference;
    ImageButton imback;
    ListView listView;
    DatabaseReference referenceTK;
    LichSuTruyCapAdapter lichSuTruyCapAdapter;
    ArrayList<LichSuTruyCap> truyCapArrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lichsutruycap);
        imback = findViewById(R.id.imgb);
        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listLS);
        Log.d("MTP", "DataFromFirebaseListener: 3");
        reference = FirebaseDatabase.getInstance().getReference().child("lichsutruycap");
        referenceTK = FirebaseDatabase.getInstance().getReference().child("taikhoan");
        Log.d("MTP", "DataFromFirebaseListener: 2");
        getDataFromFireBase();
        lichSuTruyCapAdapter = new LichSuTruyCapAdapter(LSTC.this,truyCapArrayList);
        listView.setAdapter(lichSuTruyCapAdapter);
        Log.d("MTP", "DataFromFirebaseListener: 1");
        lichSuTruyCapAdapter.notifyDataSetChanged();

    }
    public void getDataFromFireBase(){
        truyCapArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot danhsach : snapshot.getChildren()) {
                        String childKey = danhsach.getKey();
                        assert childKey != null;
                        String tenFromDatabase = snapshot.child(childKey).child("tenUser").getValue(String.class);
                        String sodienthoaiFromDatabase = snapshot.child(childKey).child("sodienthoaiUser").getValue(String.class);
                        String ngaygioFromDatabase = snapshot.child(childKey).child("ngaygio").getValue(String.class);
                        String trangthaiFromDatabase = snapshot.child(childKey).child("trangthai").getValue(String.class);
                        referenceTK.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String key = ds.getKey();
                                    String sodienthoai = snapshot.child(key).child("sodienthoai").getValue(String.class);
                                    if (sodienthoaiFromDatabase.equals(sodienthoai)) {
                                        LichSuTruyCap truyCap = new LichSuTruyCap(tenFromDatabase, sodienthoaiFromDatabase, ngaygioFromDatabase, trangthaiFromDatabase);
                                        truyCapArrayList.add(truyCap);
                                    }
                                }
                                lichSuTruyCapAdapter.notifyDataSetChanged();
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
//                        Query query = referenceTK.orderByChild("sodienthoai").equalTo(sodienthoaiFromDatabase);
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot ds : snapshot.getChildren()) {
//                                    if (snapshot.exists()){
//                                        LichSuTruyCap truyCap = new LichSuTruyCap(tenFromDatabase, sodienthoaiFromDatabase, ngaygioFromDatabase, trangthaiFromDatabase);
//                                        truyCapArrayList.add(truyCap);
//                                    }else {
//                                        reference.getRef().removeValue();
//                                    }
//                                }
//
//                                lichSuTruyCapAdapter.notifyDataSetChanged();
//                            }