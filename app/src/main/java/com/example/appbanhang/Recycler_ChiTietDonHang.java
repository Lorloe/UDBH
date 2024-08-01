package com.example.appbanhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.ChiTietHoaDon;
import com.example.appbanhang.models.HoaDon;
import com.example.appbanhang.models.SanPham;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Recycler_ChiTietDonHang extends RecyclerView.Adapter<Recycler_ChiTietDonHang.ViewHolder>{
    private Context mContext;
    private ArrayList<SanPham> sanpham;
    private ArrayList<ChiTietHoaDon> chitiet;
    public Recycler_ChiTietDonHang(Context mContext,ArrayList<SanPham> sanpham,ArrayList<ChiTietHoaDon> chitiet){
        this.mContext = mContext;
        this.sanpham = sanpham;
        this.chitiet = chitiet;
    }
    @NonNull
    @Override
    public Recycler_ChiTietDonHang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang,parent,false);
        return new Recycler_ChiTietDonHang.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_ChiTietDonHang.ViewHolder holder, int position) {
        String url = sanpham.get(position).getHinhSP();
        Picasso.with(mContext).load(url).into(holder.imvHinhSP);
        holder.txtTenSP.setText(sanpham.get(position).getTenSP());
        holder.txtSoluong.setText("X"+chitiet.get(position).getSoluong());
        holder.txtGiaSP.setText(sanpham.get(position).getGiaSP()+"đ");
    }

    @Override
    public int getItemCount() {
        return sanpham.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imvHinhSP;
        TextView txtTenSP,txtGiaSP,txtSoluong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvHinhSP = itemView.findViewById(R.id.imvHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiasp);
            txtSoluong = itemView.findViewById(R.id.txtsoluong);
        }
    }
}
