package com.example.appbanhang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.appbanhang.models.SanPham;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class gioHangAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    ArrayList<SanPham> sanPham;
    private int tongtien;
    public static final  String SHARED_PREFS = "sharedPrefs";
    private TextView txttongtien;
    public gioHangAdapter(Context context, ArrayList<SanPham> sanPham,TextView txttongtien ) {
        this.context = context;
        this.sanPham = sanPham;
        this.txttongtien = txttongtien;
    }

    @Override
    public int getCount() {
        return this.sanPham.size();
    }

    @Override
    public Object getItem(int position) {
        return this.sanPham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SanPham currentItem = (SanPham) getItem(position);
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_giohang, null);
        }
        ImageView imageView = convertView.findViewById(R.id.image_hinhgiohang);
        TextView textView = convertView.findViewById(R.id.txttengiohang);
        textView.setText(currentItem.getTenSP());
        TextView textView1 = convertView.findViewById(R.id.txtgiagiohang);
        textView1.setText(String.valueOf(currentItem.getGiaSP()));
        Button button = convertView.findViewById(R.id.btncong);
        TextView textView2 = convertView.findViewById(R.id.txtsoluong);
        textView2.setText("số lượng: " + currentItem.getSoluong());
        Button button2 = convertView.findViewById(R.id.btntru);
        MainActivity.listGH.forEach(sp -> {
            if(sp.getIdSP().equals(currentItem.getIdSP()) && sp.getSoluong() >= currentItem.getSoluongKho()){
                button.setEnabled(false);
                button.setBackgroundColor(context.getResources().getColor(R.color.grey));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                currentItem.setSoluong(currentItem.getSoluong()+1);
                if(currentItem.getSoluong() >= currentItem.getSoluongKho()){
                    button.setEnabled(false);
                    button.setBackgroundColor(context.getResources().getColor(R.color.grey));
                }
                textView2.setText("số lượng: " + currentItem.getSoluong());
                MainActivity.listGH.forEach(sanPham -> {
                    tongtien += (sanPham.getSoluong()*sanPham.getGiaSP());
                    txttongtien.setText("Giá: " + tongtien+" VNĐ");
                });
                gioHangTinhTien.TT = tongtien;
                saveData();
                tongtien = 0;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(currentItem.getSoluong() > 1) {
                    button.setEnabled(true);
                    button.setBackgroundColor(context.getResources().getColor(R.color.aqua));
                    currentItem.setSoluong(currentItem.getSoluong() - 1);
                    textView2.setText("số lượng: " + currentItem.getSoluong());
                    notifyDataSetChanged();
                    MainActivity.listGH.forEach(sanPham -> {
                        tongtien += (sanPham.getSoluong()*sanPham.getGiaSP());
                        txttongtien.setText("Giá: " + tongtien+" VNĐ");
                    });
                    gioHangTinhTien.TT = tongtien;
                    saveData();
                    tongtien = 0;
                }else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    MainActivity.listGH.remove(currentItem);
                                    MainActivity.listGH.forEach(sanPham -> {
                                        tongtien += (sanPham.getSoluong()*sanPham.getGiaSP());
                                        txttongtien.setText("Giá: " + tongtien+" VNĐ");
                                    });
                                    gioHangTinhTien.TT = tongtien;
                                    tongtien = 0;
                                    if(MainActivity.listGH.size() == 0){
                                        txttongtien.setText("Giá: " + 0 +" VNĐ");
                                        gioHangTinhTien.TT = 0;
                                    }
                                    saveData();
                                    notifyDataSetChanged();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    textView2.setText("số lượng: " + currentItem.getSoluong());
                                    saveData();
                                    notifyDataSetChanged();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Bạn muốn xóa sản phẩm?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }

            }
        });
        String url = currentItem.getHinhSP();
        Picasso.with(context).load(url).into(imageView);
        return convertView;
    }
    private void saveData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // creating a new variable for gson.
        Gson gson = new Gson();
        // getting data from gson and storing it in a string.
        String json = gson.toJson(MainActivity.listGH);
        //below line is to save data in shared
        //prefs in the form of string.
        editor.putString("listGH", json);
        editor.apply();
    }
}
