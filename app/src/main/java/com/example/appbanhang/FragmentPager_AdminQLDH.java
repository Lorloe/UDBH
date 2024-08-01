package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentPager_AdminQLDH extends FragmentPagerAdapter {
    ArrayList<Fragment> mFragmentList;
    ArrayList<String> mFragmentTittleList;
    private String[] tabTitles = new String[]{"Chờ xác nhận","Đang giao","Đã giao","Đã Hủy"};
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    public FragmentPager_AdminQLDH(@NonNull FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentTittleList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_AdminChoXacNhan();
            case 1:
                return new Fragment_AdminDangGiao();
            case 2:
                return new Fragment_AdminDaGiao();
            case 3:
                return new Fragment_AdminDaHuy();
            default:
                throw new RuntimeException("Invalid tab position");
        }
    }
    @Override
    public int getCount() {
        return 4;
    }
    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTittleList.add(title);
    }
}
