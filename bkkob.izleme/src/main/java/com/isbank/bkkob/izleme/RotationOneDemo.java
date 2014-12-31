package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.widget.Button;

public class RotationOneDemo extends Activity {
    static final int PICK_REQUEST=1337;
    Button viewButton=null;
    Uri contact=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewButton=(Button)findViewById(R.id.view);
        restoreMe(savedInstanceState);
        viewButton.setEnabled(contact!=null);
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode==PICK_REQUEST) {
            if (resultCode==RESULT_OK) {
                contact=data.getData();
                viewButton.setEnabled(true);
            }
        }
    }

    public void pickContact(View v) {
        Intent i=new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_REQUEST);
    }

    public void viewContact(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, contact));
    }

    /*
    @Override
    public Object onRetainNonConfigurationInstance() {
        return(contact);
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contact!=null) {
            outState.putString("contact", contact.toString());
        }
    }

    private void restoreMe(Bundle state) {
        contact=null;

        if (state!=null) {
            String contactUri=state.getString("contact");
            if (contactUri!=null) {
                contact=Uri.parse(contactUri);
            }
        }

        if (contact == null)
        {
            if (getLastNonConfigurationInstance()!=null)
                contact=(Uri)getLastNonConfigurationInstance();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rotation_one_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}



