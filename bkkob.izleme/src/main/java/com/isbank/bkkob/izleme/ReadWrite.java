package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ReadWrite extends Activity {
    private final static String NOTES="notes.txt";
    private EditText editor;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.read_write);
        editor=(EditText)findViewById(R.id.editor);
    }
    public void onResume() {
        super.onResume();
        try {
            java.io.File x  = getFilesDir();
            java.io.File y = Environment.getExternalStorageDirectory();

            InputStream in=openFileInput(NOTES);
            if (in!=null) {
                InputStreamReader tmp=new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String str;
                StringBuilder buf=new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str+"\n");
                }
                in.close();
                editor.setText(buf.toString());
            }
        }
        catch (java.io.FileNotFoundException e) {
// that's OK, we probably haven't created it yet
        }
        catch (Throwable t) {
            Toast
                    .makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void onPause() {
        super.onPause();
        try {
            OutputStreamWriter out=
                    new OutputStreamWriter(openFileOutput(NOTES, 0));
            out.write(editor.getText().toString());
            out.close();
        }
        catch (Throwable t) {
            Toast
                    .makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}