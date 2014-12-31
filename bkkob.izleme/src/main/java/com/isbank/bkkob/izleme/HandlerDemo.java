package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerDemo extends Activity {

    ProgressBar bar;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            bar.incrementProgressBy(5);
        }
    };
    AtomicBoolean isRunning=new AtomicBoolean(false);

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.handler_demo);
        bar=(ProgressBar)findViewById(R.id.progress);
    }

    public void onStart() {
        super.onStart();
        bar.setProgress(0);
        ProgressBaslat();
    }

    public void ProgressBaslat(){
        Thread background=new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i=0;i<20 && isRunning.get();i++) {
                        Thread.sleep(1000);
                        handler.sendMessage(handler.obtainMessage());
                    }
                }
                catch (Throwable t) {
// just end the background thread
                }
            }
        });
        isRunning.set(true);
        background.start();
    }
    public void onStop() {
        super.onStop();
        isRunning.set(false);
    }

    public void MenuClick(View view) {
        Integer id =view.getId();
        String islem = getResources().getResourceEntryName(id);

        if (islem.equals("btnDurdur"))
            isRunning.set(false);
        else if (islem.equals("btnBaslat")){
            isRunning.set(true);
            ProgressBaslat();
        }
    }

}
