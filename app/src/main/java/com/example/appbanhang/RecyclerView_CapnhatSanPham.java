package com.example.appbanhang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.SanPham;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerView_CapnhatSanPham extends RecyclerView.Adapter<RecyclerView_CapnhatSanPham.ViewHolder>{
    private final Context mContext;
    private final ArrayList<SanPham> sanphamArrayList;
    public RecyclerView_CapnhatSanPham(Context mContext, ArrayList<SanPham> sanphamArrayList) {
        this.mContext = mContext;
        this.sanphamArrayList = sanphamArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capnhatsanpham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_CapnhatSanPham.ViewHolder holder, int position) {
        String url = sanphamArrayList.get(position).getHinhSP();
        Picasso.with(mContext).load(url).into(holder.imvHinhSP);
        holder.txtTenSP.setText(sanphamArrayList.get(position).getTenSP());
        holder.txtGiasp.setText(String.valueOf(sanphamArrayList.get(position).getGiaSP()));
        holder.txtsoluong.setText(String.valueOf(sanphamArrayList.get(position).getSoluongKho()));
        holder.relative_capnhatsanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, activity_capnhatsanpham.class);
                intent.putExtra("tenSP",sanphamArrayList.get(position).getTenSP());
                intent.putExtra("idSP",sanphamArrayList.get(position).getIdSP());
                intent.putExtra("giaSP",sanphamArrayList.get(position).getGiaSP());
                intent.putExtra("motaSP",sanphamArrayList.get(position).getMotaSP());
                intent.putExtra("soluongKho",sanphamArrayList.get(position).getSoluongKho());
                intent.putExtra("hinhSP",sanphamArrayList.get(position).getHinhSP());
                intent.putExtra("idTH",sanphamArrayList.get(position).getIdTH());
                intent.putExtra("giaGoc",sanphamArrayList.get(position).getGiaGoc());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanphamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvHinhSP;
        TextView txtTenSP,txtsoluong,txtGiasp;
        RelativeLayout relative_capnhatsanpham;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relative_capnhatsanpham = itemView.findViewById(R.id.relative_capnhatsanpham);
            imvHinhSP = itemView.findViewById(R.id.imvHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtsoluong = itemView.findViewById(R.id.txtsoluong);
            txtGiasp = itemView.findViewById(R.id.txtGiasp);
        }
    }
}
