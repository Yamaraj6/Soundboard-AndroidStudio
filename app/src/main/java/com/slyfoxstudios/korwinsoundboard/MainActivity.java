package com.slyfoxstudios.korwinsoundboard;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements IRateUsClicksListener
{
    public static Ads ads;
    public static MediaPlayerPlay _mpp;
    public static boolean RATED;
    public static boolean REFRESHED;
    public static SharedPreferences mPrefs;
    public static int clicks;
    Dialog dialog;

    @Override
    protected void onResume()
    {
        super.onResume();
        REFRESHED = mPrefs.getBoolean("refreshed", false);
        if(RATED&&!REFRESHED)
        {
            RefreshFragment();
            REFRESHED = true;
            SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();
            mEditor.putBoolean("refreshed", REFRESHED).commit();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _mpp = new MediaPlayerPlay(this);
        mPrefs = getSharedPreferences(getResources().getString(R.string.app_name), 0);
        REFRESHED = mPrefs.getBoolean("refreshed", false);
        RATED = mPrefs.getBoolean("rated", false);
        clicks = mPrefs.getInt("clicks", 0);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(this, getSupportFragmentManager(),this.getLayoutInflater()));
        vHandleDB();
    }

    private void vHandleDB()
    {
        DatabaseBackend dbBackend = new DatabaseBackend(this);
        try
        {
            dbBackend.CreateDatabse();
        } catch (IOException ioe)
        {
            throw new Error("Unable to create database");
        }

        try
        {
            dbBackend.openDataBase();
        } catch (SQLiteException sqle)
        {
            throw new Error("Could not open database");
        }
    }

    @Override
    public void CheckIfRated(int clicks)
    {

        //  RATED = mPrefs.getBoolean("rated", false);
        // clicks = mPrefs.getInt("clicks",0);
        if(!RATED && (clicks==(getResources().getInteger(R.integer.clicksforrate)) || clicks==(getResources().getInteger(R.integer.clicksforrate2))))
            RateUs();
    }
    public void RateUs()
    {
        View popup = getLayoutInflater().inflate(R.layout.rate_us_popup, null);
        Button btnRateus = (Button) popup.findViewById(R.id.img_rateus);
        TextView txtNotNow = (TextView) popup.findViewById(R.id.txtNotNow);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(popup);
        alert.setCancelable(false);

        dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        txtNotNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        btnRateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!RATED)
                {
                    RATED = true;
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putBoolean("rated", RATED).commit();
                }
                Uri uri = Uri.parse(getResources().getString(R.string.rate_us_1));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try
                {
                    // RefreshFragment();
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e)
                {
                    //  RefreshFragment();
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getResources().getString(R.string.rate_us_2))));
                }
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener()
        {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    //  finish();
                    dialog.dismiss();
                }
                return true;
            }
        });
    }


    private void RefreshFragment()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}

