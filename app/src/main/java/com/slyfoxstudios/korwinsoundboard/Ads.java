package com.slyfoxstudios.korwinsoundboard;

import android.content.Context;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

/**
 * Created by Alex on 05.03.2017.
 */
public class Ads
{
    private int CLICK_FOR_AD = 15;
    private int iTurnAds;
    private Context _context;
    private AdView _adView;
    private InterstitialAd interstitial;
    private MediaPlayerPlay _mpp;

    public Ads(Context context, AdView adView, MediaPlayerPlay mpp)
    {
        iTurnAds = 7;

        _context = context;
        _adView = adView;
        _mpp = mpp;

        if(MainActivity.clicks > _context.getResources().getInteger(R.integer.clicksforrate))
            CLICK_FOR_AD=12;
    }

    public void showBanner()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        _adView.loadAd(adRequest);
    }

    public void displayInterstitial()
    {
        iTurnAds++;

        if (iTurnAds >= CLICK_FOR_AD)
        {
            if(MainActivity.clicks > _context.getResources().getInteger(R.integer.clicksforrate))
            {
                Random generator = new Random();
                CLICK_FOR_AD = generator.nextInt(8) + 7;
            //    Log.d(CLICK_FOR_AD + "", "kurwa");
            }

            AdRequest adRequest = new AdRequest.Builder().build();

            // Prepare the Interstitial Ad
            interstitial = new InterstitialAd(_context);
            interstitial.setAdUnitId(_context.getString(R.string.admob_interstitial_id));
            interstitial.loadAd(adRequest);

            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    if (interstitial.isLoaded())    // If Ads are loaded, show Interstitial else show nothing.
                    {
                        interstitial.show();
                        iTurnAds=0;
                    }
                }
            });
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    // Do something after 5s = 5000ms
                    interstitial.setAdListener(new AdListener()
                    {
                        public void onAdLoaded()
                        {
                            if (interstitial.isLoaded())    // If Ads are loaded, show Interstitial else show nothing.
                            {
                                interstitial.show();
                                iTurnAds=0;
                            }
                        }
                    });
                }
            }, 1000);
        }

    }


}
