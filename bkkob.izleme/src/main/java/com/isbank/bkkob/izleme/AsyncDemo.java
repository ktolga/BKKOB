package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;

public class AsyncDemo extends ListActivity {
    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer",
            "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae",
            "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat",
            "placerat", "ante",
            "porttitor", "sodales",
            "pellentesque", "augue",
            "purus"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_demo);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList()));
        new AddStringTask().execute();
    }

    class AddStringTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... unused) {
            for (String item : items) {
                publishProgress(item);
                SystemClock.sleep(200);
            }
            return(null);
        }
        @Override
        protected void onProgressUpdate(String... item) {
            ((ArrayAdapter)getListAdapter()).add(item[0]);
        }
        @Override
        protected void onPostExecute(Void unused) {
            Toast
                    .makeText(AsyncDemo.this, "Done!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
