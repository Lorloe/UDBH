package com.example.appbanhang;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.models.SanPham;
import com.example.appbanhang.ultis.XuLyChuoiTiengViet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Search extends RecyclerView.Adapter<RecyclerView_Search.ViewHolder> {
    @NonNull
    private Context mcontext;
    ArrayList<SanPham> list;
    ArrayList<SanPham> filterList;

    XuLyChuoiTiengViet xuLyChuoiTiengViet = new XuLyChuoiTiengViet();
    public RecyclerView_Search(Context mcontext, ArrayList<SanPham> list) {
        this.mcontext = mcontext;
        this.filterList = list;
        this.list = list;
    }

    public RecyclerView_Search.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new RecyclerView_Search.ViewHolder(view);
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_Search.ViewHolder holder, int position) {
        String url = filterList.get(position).getHinhSP();
        Picasso.with(mcontext).load(url).into(holder.imvHSP);
        holder.txtTSP.setText(filterList.get(position).getTenSP());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext,chiTietSanPham.class);
                intent.putExtra("ten",filterList.get(position).getTenSP());
                intent.putExtra("id",filterList.get(position).getIdSP());
                intent.putExtra("hinh",filterList.get(position).getHinhSP());
                intent.putExtra("gia",filterList.get(position).getGiaSP());
                intent.putExtra("mota",filterList.get(position).getMotaSP());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvHSP;
        TextView txtTSP;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvHSP = itemView.findViewById(R.id.imvHSP);
            txtTSP = itemView.findViewById(R.id.txtTSP);
            relativeLayout = itemView.findViewById(R.id.relative_item_search);
        }
    }
    private Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            /// khởi tạo biến result
            FilterResults filterResults= new FilterResults();
            String keySearch = constraint.toString();
            /// khi keysearch co gia tri
            if (keySearch != null && keySearch.toString().length() > 0) {
                /// thì mình khởi tạo list trống để add dữ liệu sao khi check vào
                ArrayList<SanPham> filteredList = new ArrayList<>();
                String pattrn= keySearch.toLowerCase().trim();
                for(SanPham item : list){
                    /// check dử liệu để add
                    if (xuLyChuoiTiengViet.ConvertString(item.getTenSP().toLowerCase())
                            .contains(xuLyChuoiTiengViet.ConvertString(pattrn)) == true) {
                        filteredList.add(item);
                    }
                }
                /// gán vào giá trị sao khi check xong
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }
            else{
                /// gán lại giá trị all
                filterResults.values = list;
                filterResults.count = list.size();
//                synchronized (list) {
//                    filterResults.values = list;
//                    filterResults.count = list.size();
//                }
            }
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<SanPham>) results.values;
            notifyDataSetChanged();
        }
    };
}
