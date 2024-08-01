package com.example.appbanhang;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.models.SanPham;

public class ListFavorite extends Fragment {
    GridView gridView;
    sanPhamAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfavorite, container , false);
        gridView = (GridView) view.findViewById(R.id.grid_view_yeuthich);
        adapter = new sanPhamAdapter(getActivity(),MainActivity.listYT);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SanPham spYT = MainActivity.listYT.get(position);
                Intent intent = new Intent(getContext(),chiTietSanPham.class);
                intent.putExtra("id", spYT.getIdSP());
                intent.putExtra("hinh",spYT.getHinhSP());
                intent.putExtra("ten",spYT.getTenSP());
                intent.putExtra("mota",spYT.getMotaSP());
                intent.putExtra("gia",spYT.getGiaSP());
                intent.putExtra("soluongKho",spYT.getSoluongKho());
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

}
