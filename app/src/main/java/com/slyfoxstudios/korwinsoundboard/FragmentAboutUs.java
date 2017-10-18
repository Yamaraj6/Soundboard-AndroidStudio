package com.slyfoxstudios.korwinsoundboard;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.gms.ads.AdView;

/**
 * Created by user on 04.04.2017.
 */
public class FragmentAboutUs extends Fragment
{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_layout_aboutus, container, false);
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        MainActivity.ads = new Ads(getContext(), mAdView, MainActivity._mpp);
        Buttons();
        ShowBanner();
        return view;
    }

    public void ShowBanner()
    {
        if (getResources().getBoolean(R.bool.ads_on))
            MainActivity.ads.showBanner();
    }

    private void Buttons()
    {
        LinearLayout btnRateus = (LinearLayout) view.findViewById(R.id.img_rateus);
        btnRateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.RATED)
                {
                    MainActivity.RATED = true;
                    SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();
                    mEditor.putBoolean("rated", MainActivity.RATED).commit();
                }
                Uri uri = Uri.parse(getResources().getString(R.string.rate_us_1));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getResources().getString(R.string.rate_us_2))));
                }
            }
        });

        LinearLayout btnFace = (LinearLayout) view.findViewById(R.id.img_facebook);
        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.facebook_1));
                Intent goToFacebookApp = new Intent(Intent.ACTION_VIEW, uri);
                goToFacebookApp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(goToFacebookApp);
                } catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getResources().getString(R.string.facebook_2))));
                }
            }
        });


        LinearLayout btnGp = (LinearLayout) view.findViewById(R.id.img_google_play);
        btnGp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.google_play));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getResources().getString(R.string.google_play2))));
                }
            }
        });
    }
}
