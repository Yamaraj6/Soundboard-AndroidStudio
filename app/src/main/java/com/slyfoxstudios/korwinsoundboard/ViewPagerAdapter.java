package com.slyfoxstudios.korwinsoundboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

/**
 * Created by user on 04.04.2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter
{
    int _i_screnn_numbers;
    Context _context;
    LayoutInflater _layoutInflater;
    private String tabtitles[] = new String[3];

    public ViewPagerAdapter(Context context, FragmentManager fm, LayoutInflater layInf) {
        super(fm);
        _context=context;
        _i_screnn_numbers=_context.getResources().getInteger(R.integer.screen_count);
        _layoutInflater = layInf;
        tabtitles = new String[_i_screnn_numbers];
        for(int i = 0; i<_context.getResources().getInteger(R.integer.screen_count); i++)
        {
            switch (i)
            {
                case 0:
                    tabtitles[i] = _context.getResources().getString(R.string.tab_name_1);
                    break;
                case 1:
                    tabtitles[i] = _context.getResources().getString(R.string.tab_name_2);
                    break;
                case 2:
                    tabtitles[i] = _context.getResources().getString(R.string.tab_name_3);
                    break;
                default:
                    tabtitles[i] = _context.getResources().getString(R.string.tab_name_aboutus);
                    break;

            }
        }
    }
    @Override
    public int getCount() {return _i_screnn_numbers;}

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        int []listNumber= new int[5];
        switch (position) {
            case 0:
                bundle.putInt("lists_on_screen", _context.getResources().getInteger(R.integer.lists_on_screen_1));
                bundle.putInt("fragment_layout", R.layout.fragment_layout_1);
                bundle.putInt("screen", R.id.screen1);
                bundle.putInt("screen_number", 1);
                bundle.putInt("list", R.id.lst_1);
                listNumber[0]=R.string.screen_1_list_1;
                listNumber[1]=R.string.screen_1_list_2;
                listNumber[2]=R.string.screen_1_list_3;
                listNumber[3]=R.string.screen_1_list_4;
                listNumber[4]=R.string.screen_1_list_5;

                bundle.putIntArray("list_number", listNumber);
                FragmentWithList fragment1 = new FragmentWithList();
                fragment1.setArguments(bundle);
                return fragment1;
            case 1:
                bundle.putInt("lists_on_screen", _context.getResources().getInteger(R.integer.lists_on_screen_2));
                bundle.putInt("fragment_layout", R.layout.fragment_layout_2);
                bundle.putInt("screen", R.id.screen2);
                bundle.putInt("screen_number", 2);
                bundle.putInt("list", R.id.lst_2);
                listNumber[0]=R.string.screen_2_list_1;
                listNumber[1]=R.string.screen_2_list_2;
                listNumber[2]=R.string.screen_2_list_3;
                listNumber[3]=R.string.screen_2_list_4;
                listNumber[4]=R.string.screen_2_list_5;

                bundle.putIntArray("list_number", listNumber);
                FragmentWithList fragment2 = new FragmentWithList();
                fragment2.setArguments(bundle);
                return fragment2;
            case 2:
                if(MainActivity.RATED)
                    bundle.putInt("lists_on_screen", _context.getResources().getInteger(R.integer.lists_on_screen_3));
                else
                    bundle.putInt("lists_on_screen", _context.getResources().getInteger(R.integer.lists_on_screen_3)-1);
                bundle.putInt("fragment_layout", R.layout.fragment_layout_3);
                bundle.putInt("screen", R.id.screen3);
                bundle.putInt("screen_number", 3);
                bundle.putInt("list", R.id.lst_3);
                listNumber[0]=R.string.screen_3_list_1;
                listNumber[1]=R.string.screen_3_list_2;
                listNumber[2]=R.string.screen_3_list_3;
                listNumber[3]=R.string.screen_3_list_4;
                listNumber[4]=R.string.screen_3_list_5;

                bundle.putIntArray("list_number", listNumber);
                FragmentWithList fragment3 = new FragmentWithList();
                fragment3.setArguments(bundle);
                return fragment3;
            default:
                FragmentAboutUs fragmentlast = new FragmentAboutUs();
                return fragmentlast;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
