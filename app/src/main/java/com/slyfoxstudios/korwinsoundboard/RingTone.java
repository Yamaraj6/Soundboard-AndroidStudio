package com.slyfoxstudios.korwinsoundboard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Alex on 24.03.2017.
 */
public class RingTone
{
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String _s_path;
    private Context _context;

    public RingTone(Context context)
    {
        this._context = context;
        verifyStoragePermissions((Activity) _context);
        this._s_path = _context.getResources().getString(R.string.path_to_raw_folder);
    }

    //sName przekazywać bez .mp3
    public void ChangeRingTone(int iType, String sName, String sTitle)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                if (Settings.System.canWrite(_context))
                {
                    setRingtone(iType, sName,sTitle);
                }
                else
                {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setData(Uri.parse("package:" + _context.getPackageName()))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    _context.startActivity(intent);
                }


                Log.e("value", "Permission already Granted, Now you can save image.");
            } else {
                requestPermission();
            }
        } else {
            setRingtone(iType , sName, sTitle);
            Log.e("value", "Not required for requesting runtime permission");
        }
    }

    private void setRingtone(int iType, String sName, String sTitle)
    {
        Random random=new Random();
        String sName2=sName+Math.abs(random.nextInt()%1000);
        AssetFileDescriptor openAssetFileDescriptor;
        ((AudioManager) _context.getSystemService(_context.AUDIO_SERVICE)).setRingerMode(2);
        File file = new File(Environment.getExternalStorageDirectory() + _context.getResources().getString(R.string.folder_with_ringtones), sName2+".mp3");

        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Uri parse = Uri.parse(_s_path + sName);

        ContentResolver contentResolver = _context.getContentResolver();
        try
        {
            openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(parse, "r");
        } catch (FileNotFoundException e2)
        {
            openAssetFileDescriptor = null;
        }
        try
        {
            byte[] bArr = new byte[1024];
            FileInputStream createInputStream = openAssetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            for (int read = createInputStream.read(bArr); read != -1; read = createInputStream.read(bArr))
            {
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.close();
        } catch (IOException e3)
        {
            e3.printStackTrace();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        contentValues.put(MediaStore.MediaColumns.TITLE, sTitle);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        contentValues.put(MediaStore.MediaColumns.SIZE, file.length());
        contentValues.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        contentValues.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        contentValues.put(MediaStore.Audio.Media.IS_ALARM, true);
        contentValues.put(MediaStore.Audio.Media.IS_MUSIC, false);

        try
        {
            RingtoneManager.setActualDefaultRingtoneUri(_context, iType,
                    contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentValues));
            Toast.makeText(_context, new StringBuilder().append(_context.getResources().getString(R.string.ringtone_changed)), Toast.LENGTH_LONG).show();
        } catch (Throwable th)
        {
            Toast.makeText(_context, new StringBuilder().append(_context.getResources().getString(R.string.ringtone_not_changed)), Toast.LENGTH_LONG).show();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(_context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)_context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(_context, _context.getResources().getString(R.string.permission), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity)_context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}