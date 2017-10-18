package com.slyfoxstudios.korwinsoundboard;

/**
 * Created by Alex on 12.04.2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splashscreen extends Activity
{
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashThread;
    Object Finished;
    Animation anim;
    boolean is_finished;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Finished=new Object();
        is_finished=false;

        StartAnimations();
    }
    private void StartAnimations() {
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                    is_finished = false;
                    int waited = 0;
                    // Splash screen pause time
                    synchronized (Finished)
                    {
                        try
                        {
                            while (!is_finished && waited < 1650)
                            {
                                sleep(100);
                                waited += 100;
                            }
                        } catch (InterruptedException e)
                        {
                        }finally {
                        }
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
            }
        };
        splashThread.start();
    }
}