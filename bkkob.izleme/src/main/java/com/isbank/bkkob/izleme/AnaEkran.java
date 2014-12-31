package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;
import com.isbank.framework.Ldap.EmployeeInfo;
import android.widget.TextView;

public class AnaEkran extends Activity {

    //public final static String TRIAD_OTELEME_EKRANI = "com.example.myfirstapp.TRIADOTELEME";
    private String kullaniciAdi="";
    private String sifre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Intent intent = getIntent();
        String kullaniciAdi = intent.getStringExtra(Sabitler.KULLANICI_ADI);
        String sifre = intent.getStringExtra(Sabitler.KULLANICI_SIFRE);
        String fullName = intent.getStringExtra(Sabitler.KULLANICI_BILGISI);

        if (fullName != null && fullName.trim().length() > 0) {
            TextView txtKullanici = (TextView)findViewById(R.id.txtKullaniciBilgi);
            txtKullanici.setText(String.format("Hoş geldiniz %s", fullName));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ana_ekran2, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ana_ekran, container, false);
            return rootView;
        }
    }

    public void MenuClick(View view) {
        Integer id =view.getId();
        String islem = getResources().getResourceEntryName(id);
        String metin = "", metin2="menüsü yapım aşamasındadır";

        if (islem.equals("btnTriadAksiyonOteleme"))
        {
            //metin = String.format("\"%s\" %s", "Triad Aksiyon Öteleme", metin2);
            Intent intent = new Intent(this, triad_aksiyon_oteleme.class);
            intent.putExtra(Sabitler.KULLANICI_ADI, kullaniciAdi);
            intent.putExtra(Sabitler.KULLANICI_SIFRE, sifre);
        }
        else if (islem.equals("btnDinamikRaporlar"))
            metin = String.format("\"%s\" %s", "Dinamik Raporlar", metin2);
        else if (islem.equals("btnMenu1"))
            metin = "Bu menü kullanılmamaktadır";
        else if (islem.equals("btnMenu2"))
            metin = "Bu menü kullanılmamaktadır";
        else
            metin = islem;

        Toast.makeText(getApplicationContext(), metin, Toast.LENGTH_SHORT).show();
    }
    /*
    public void btnIslemGerceklestir_Click(View view) {
        RadioGroup  rdgSecim = (RadioGroup ) findViewById(R.id.rdgIslemSecimi);

        switch (rdgSecim.getCheckedRadioButtonId())
        {
            case 0:
                //Triad aksiyon öteleme
                //Intent intent = new Intent(this, TriadAksiyonOteleme.class);
                //startActivity(intent);
                break;

            case 1:
                break;

        }
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
        */
}
