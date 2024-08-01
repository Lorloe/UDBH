package com.example.appbanhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appbanhang.models.LichSuTruyCap;
import com.example.appbanhang.models.SanPham;

import java.util.ArrayList;

public class LichSuTruyCapAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    ArrayList<LichSuTruyCap> lichSuTruyCaps;

    public LichSuTruyCapAdapter(Context context, ArrayList<LichSuTruyCap> lichSuTruyCaps) {
        this.context = context;
        this.lichSuTruyCaps = lichSuTruyCaps;
    }

    @Override

    public int getCount() {
        return this.lichSuTruyCaps.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lichSuTruyCaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LichSuTruyCap currentItem = (LichSuTruyCap) getItem(position);
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemlstc, null);
        }
        TextView txtLSten = convertView.findViewById(R.id.txtLSten);
        txtLSten.setText(currentItem.getTenUser());
        TextView txtLSsodienthoai = convertView.findViewById(R.id.txtLSsodienthoai);
        txtLSsodienthoai.setText(currentItem.getSodienthoaiUser());
        TextView txtLSngaygio = convertView.findViewById(R.id.txtLSngaygio);
        txtLSngaygio.setText(currentItem.getNgaygio());
        TextView txtLStrangthai = convertView.findViewById(R.id.txtLStrangthai);
        txtLStrangthai.setText(currentItem.getTrangthai());
        return convertView;
    }
}
