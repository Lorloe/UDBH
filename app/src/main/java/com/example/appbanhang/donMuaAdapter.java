package com.example.appbanhang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.SanPham;
import com.example.appbanhang.models.SanPhamDonMua;
import com.example.appbanhang.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class donMuaAdapter extends RecyclerView.Adapter<donMuaAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<SanPhamDonMua> sanphamList;

    @Override
    public int getItemCount() {
        return sanphamList.size();
    }

    public donMuaAdapter(Context mContext, ArrayList<SanPhamDonMua> sanphamList) {
        this.mContext = mContext;
        this.sanphamList = sanphamList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdonmua,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MTL", "onBindViewHolder: " + position);
        String url = sanphamList.get(position).getHinh();
        Picasso.with(mContext).load(url).into(holder.hinh_donmua);
        holder.txttendonmua.setText(sanphamList.get(position).getTen());
        holder.txtgiadonmua.setText(String.valueOf(sanphamList.get(position).getGia())+"đ");
        holder.txtsoluongdonmua.setText("X"+String.valueOf(sanphamList.get(position).getSoluong()));
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView hinh_donmua;
        TextView txttendonmua,txtgiadonmua,txtsoluongdonmua;
        RelativeLayout rlItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgiadonmua = itemView.findViewById(R.id.txtgiadonmua);
            txtsoluongdonmua = itemView.findViewById(R.id.txtsoluongdonmua);
            txttendonmua = itemView.findViewById(R.id.txttendonmua);
            hinh_donmua = itemView.findViewById(R.id.image_hinhdonmua);
        }

    }
}
