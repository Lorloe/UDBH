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
import com.example.appbanhang.models.ThuongHieu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Recyclerview_CapnhatThuongHieu extends RecyclerView.Adapter<Recyclerview_CapnhatThuongHieu.ViewHolder>{
    private Context mContext;
    private ArrayList<ThuongHieu> list;
    public Recyclerview_CapnhatThuongHieu(Context mContext, ArrayList<ThuongHieu> list) {
        this.mContext = mContext;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capnhatthuonghieu,parent,false);
        return new Recyclerview_CapnhatThuongHieu.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recyclerview_CapnhatThuongHieu.ViewHolder holder, int position) {
        String url = list.get(position).getHinhTH();
        Picasso.with(mContext).load(url).into(holder.imvHinhTH);
        holder.relative_capnhatthuonghieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,activity_capnhatthuonghieu.class);
                intent.putExtra("idTH",list.get(position).getID());
                intent.putExtra("tenTH",list.get(position).getTenTH());
                intent.putExtra("hinhTH",list.get(position).getHinhTH());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_capnhatthuonghieu;
        ImageView imvHinhTH;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relative_capnhatthuonghieu = itemView.findViewById(R.id.relative_capnhatthuonghieu);
            imvHinhTH = itemView.findViewById(R.id.imvHinhTH);
        }
    }
}
