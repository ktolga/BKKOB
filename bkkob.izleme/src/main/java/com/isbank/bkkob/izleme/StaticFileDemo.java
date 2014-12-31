package com.isbank.bkkob.izleme;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StaticFileDemo extends ListActivity {
    TextView selection;
    ArrayList<String> items=new ArrayList<String>();
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.static_file_demo);
        selection=(TextView)findViewById(R.id.selection);
        try {
            InputStream in=getResources().openRawResource(R.raw.words);
            DocumentBuilder builder=DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc=builder.parse(in, null);
            NodeList words=doc.getElementsByTagName("word");
            for (int i=0;i<words.getLength();i++) {
                items.add(((Element)words.item(i)).getAttribute("value"));
            }
            in.close();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                items));
    }
    public void onListItemClick(ListView parent, View v, int position,
                                long id) {
        selection.setText(items.get(position).toString());
    }
}