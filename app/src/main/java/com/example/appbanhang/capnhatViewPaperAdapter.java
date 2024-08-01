package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class capnhatViewPaperAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> mFragmentList;
    ArrayList<String> mFragmentTittleList;
    private String[] tabTitles = new String[]{"Sản phẩm","Thương hiệu"};

    public capnhatViewPaperAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentTittleList = new ArrayList<>();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_CapNhatSanPham();
            case 1:
                return new Fragment_CapNhatThuongHieu();
            default:
                throw new RuntimeException("Invalid tab position");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTittleList.add(title);
    }
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
