package com.slyfoxstudios.korwinsoundboard;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by user on 05.03.2017.
 */
public class MediaPlayerPlay {

    public MediaPlayer _mp = new MediaPlayer();
    final private Context _context;
    final private String PACKAGE_NAME;

    public MediaPlayerPlay(Context context)
    {
        this._context = context;
        PACKAGE_NAME = context.getPackageName();
    }

    public void Play(String Id, DatabaseHelper db)
    {
        _mp.release();

        String direction = db.GetSoundDirById(Id);
        int resID = _context.getResources().getIdentifier(direction, "raw", PACKAGE_NAME);
        _mp = MediaPlayer.create(_context, resID);
        _mp.start();
    }
}
