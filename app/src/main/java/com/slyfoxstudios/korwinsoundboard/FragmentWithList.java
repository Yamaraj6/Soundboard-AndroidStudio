package com.slyfoxstudios.korwinsoundboard;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by user on 04.04.2017.
 */
public class FragmentWithList extends Fragment
{
    ExpandableListAdapter list_adapter;
    ExpandableListView exp_list_view;
    View view;
    DatabaseHelper my_db;
    List<String> list_data_header;
    HashMap<String, List<String>> listDataChild;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(getArguments().getInt("fragment_layout"), container, false);
        AdView adView = (AdView) view.findViewById(R.id.adView);
        MainActivity.ads = new Ads(getContext(), adView, MainActivity._mpp);
        my_db = new DatabaseHelper(getActivity());
        vPrepareData();
        vShowBanner();
        if(getArguments().getInt("fragment_layout") == R.layout.fragment_layout_1)
            ((TextView) view.findViewById(R.id.click_counter)).setText("" + MainActivity.clicks);

        return view;
    }


    public void vShowBanner()
    {
        if (getResources().getBoolean(R.bool.ads_on))
            MainActivity.ads.showBanner();
    }

    private void vPrepareData()
    {
        exp_list_view = ((ExpandableListView) view.findViewById(getArguments().getInt("list")));
        (view.findViewById(getArguments().getInt("screen"))).setVisibility(View.VISIBLE);
        vFillExpandableListView(exp_list_view, getArguments().getInt("screen_number"), getArguments().getInt("lists_on_screen"));
    }

    private void vFillExpandableListView(ExpandableListView e, Integer screenNumber, Integer listsOnScreen)
    {
        vPrepareListData(screenNumber, listsOnScreen, getArguments().getIntArray("list_number"));
        list_adapter = new ExpandableListAdapter(getActivity(), getTargetFragment(),list_data_header,listDataChild,MainActivity._mpp,MainActivity.ads, getActivity().getLayoutInflater(),my_db);
        e.setAdapter(list_adapter);
    }

    private void vPrepareListData(Integer screenNumber, Integer listsOnScreen, int[] list_number)
    {
        list_data_header = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> list[] = new List[listsOnScreen];
        for (int i = 0; i < listsOnScreen; i++)
            list[i] = new ArrayList<>();

        Cursor curs;
        for (int i = 0; i < listsOnScreen; i++)
        {
            curs = my_db.getDataToExpandableLists(i+1, screenNumber);
            while (curs.moveToNext())
                list[i].add(curs.getString(0) + ". " + curs.getString(1));
        }
                for(int i = 0; i <  listsOnScreen; i++)
                {
                    switch (i)
                    {
                        case 0:
                            list_data_header.add(getResources().getString(list_number[0]));
                            break;
                        case 1:
                            list_data_header.add(getResources().getString(list_number[1]));
                            break;
                        case 2:
                            list_data_header.add(getResources().getString(list_number[2]));
                            break;
                        case 3:
                            list_data_header.add(getResources().getString(list_number[3]));
                            break;
                        case 4:
                            list_data_header.add(getResources().getString(list_number[4]));
                            break;
                    }
                    listDataChild.put(list_data_header.get(i), list[i]);
        }
    }
}