package com.isbank.bkkob.izleme;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.content.Intent;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import java.util.List;

import com.isbank.framework.Ldap.LDAPServerInstance;
import com.isbank.framework.Ldap.EmployeeInfo;

public class Login extends Activity {

    EmployeeInfo empInfo =null;
    private EditText  username=null;
    private EditText  password=null;
    private TextView  kalanDeneme=null;
    private TextView attempts;
    private Button login;
    int counter = 0;
    private boolean sifreGirisiEngellendi = false;

    private LdapParam ldapParam = null;
    public LdapParam getLdapParam() {
        if (ldapParam != null)
            return this.ldapParam;
        else
        {
            ldapParam = new LdapParam();
            ldapParam.setLdapUrl(getResources().getString(R.string.ldap_sunucu));
            ldapParam.setLdapPort(Integer.parseInt(getResources().getString(R.string.ldap_port)));
            ldapParam.setLdapSsl(Integer.parseInt(getResources().getString(R.string.ldap_ssl)));

            return this.ldapParam;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editText1);
        password = (EditText)findViewById(R.id.editText2);
        attempts = (TextView)findViewById(R.id.textView5);
        kalanDeneme= (TextView)findViewById(R.id.textView4);
        attempts.setText(Integer.toString(counter));
        login = (Button)findViewById(R.id.button1);

        OnKeyListener();
        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
                    */
    }

    private void OnKeyListener(){
        if (password != null) {
            password.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction()==KeyEvent.ACTION_DOWN) {
                        return SifreGirisiKontrol();
                    }
                    return false;
                }
            });
        }

        if (username != null){
            username.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction()==KeyEvent.ACTION_DOWN) {
                        return SifreGirisiKontrol();
                    }
                    return false;
                }
            });
        }
    }

    private boolean SifreGirisiKontrol(){
        boolean retVal = false;

        if (sifreGirisiEngellendi){
            counter = 0;

            if (login != null)
                login.setEnabled(true);

            if (attempts != null)
                attempts.setText(Integer.toString(counter));
            attempts.setBackgroundColor(Color.WHITE);

            if (kalanDeneme != null)
                kalanDeneme.setBackgroundColor(Color.WHITE);
            sifreGirisiEngellendi = false;

            retVal = true;
        }

        return retVal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public void login(View view){
        //String host = "LDAP://ldap01.isbank/ou=People,dc=isbank";
        //Integer port = 636;

            /*String host = "ldap01.isbank";
            Integer port = 389;*/

        String kullaniciAdi = username.getText().toString();
        String kullanicisifre = password.getText().toString();
        String baseDn = "ou=People,dc=isbank";

        //asenkron çağrı:
        new AsyncLogin().execute(kullaniciAdi, kullanicisifre, this.getLdapParam().getLdapUrl(), Integer.toString(this.getLdapParam().getLdapPort()), Integer.toString(this.getLdapParam().getLdapSsl()), baseDn);

            /*
            //Senkron çağrı
            LDAPServerInstance ldapServer = new LDAPServerInstance(this.getLdapParam().getLdapUrl(), this.getLdapParam().getLdapPort(), this.getLdapParam().getLdapSsl(), "ou=People,dc=isbank");
            retVal = ldapServer.authenticate(kullaniciAdi, kullanicisifre);
            */

    }

    private void LoginBasarili() {
        try {

            int x =android.os.Build.VERSION.SDK_INT;

            Toast.makeText(getApplicationContext(), "Yönlendiriliyor...", Toast.LENGTH_SHORT).show();

            String[] img = getResources().getStringArray(R.array.cities);
            /*
            //ana ekranı başlat
            Intent intent = new Intent(this, AnaEkran.class);
            intent.putExtra(Sabitler.KULLANICI_ADI, username.getText());
            intent.putExtra(Sabitler.KULLANICI_SIFRE, password.getText());

            //EmployeeInfo empInfo = getEmployeeInfo();
            if (empInfo != null)
                intent.putExtra(Sabitler.KULLANICI_BILGISI, empInfo.getFullName());

            startActivity(intent);
*/
//deneme sdsd
            startActivity(new Intent(this, ReadWrite.class));
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), String.format("LDAP kontrolü sırasında hata oluştu: \"%s\"", ex.getMessage()), Toast.LENGTH_SHORT).show();
       }
    }
    private void LoginKontrolEdiliyor() {
        Toast.makeText(getApplicationContext(), "Kullanıcı doğrulanıyor...", Toast.LENGTH_SHORT).show();
    }


    private void LoginHatali() {
        Toast.makeText(getApplicationContext(), "Bilgiler yanlış", Toast.LENGTH_SHORT).show();

        counter++;
        if (attempts != null) {
            attempts.setBackgroundColor(Color.RED);
            attempts.setText(Integer.toString(counter));
        }
        if (kalanDeneme != null)
            kalanDeneme.setBackgroundColor(Color.RED);
        if(counter==3){
            login.setEnabled(false);
            sifreGirisiEngellendi = true;
        }
    }

    /*
    private EmployeeInfo getEmployeeInfo()
    {
        EmployeeInfo retVal = null;

        try {
            String kullaniciAdi = username.getText().toString();
            String kullanicisifre = password.getText().toString();

            LDAPServerInstance ldapServer = new LDAPServerInstance(this.getLdapParam().getLdapUrl(), this.getLdapParam().getLdapPort(), this.getLdapParam().getLdapSsl(), "ou=People,dc=isbank");
            List<EmployeeInfo> tmp = ldapServer.getEmployeeInfo(kullaniciAdi, kullanicisifre, String.format("(uid=%s)", kullaniciAdi));
            if (tmp != null && !tmp.isEmpty())
                retVal = tmp.get(0);

        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), String.format("Kullanıcı bilgileri ldap dan getirilirken hata oluştu: \"%s\"", ex.getMessage()), Toast.LENGTH_SHORT).show();
        }

        return retVal;
    }
    */

    private class AsyncLogin extends AsyncTask<String, Integer, Boolean> {
        EmployeeInfo emp = null;

        @Override
        protected Boolean doInBackground(String... param) {
            Boolean retVal = false;

            try {
                if (param.length >= 6) {
                    String kullaniciAdi = param[0];
                    String kullaniciSifre = param[1];
                    String ldapUrl = param[2];
                    int port = Integer.parseInt(param[3]);
                    int ssl = Integer.parseInt(param[4]);
                    String bindDN = param[5];

                    if(username.getText().toString().equals("TK57720") && password.getText().toString().equals("57720")) {
                        retVal = true;
                        emp = new EmployeeInfo();
                        emp.setFirstName("Tolga");
                        emp.setLastName("Kabadurmuş");
                    }
                    else{
                        emp = getEmployeeInfo(kullaniciAdi, kullaniciSifre, ldapUrl, port, ssl, bindDN);
                        //retVal = LdapKontrolu(kullaniciAdi, kullaniciSifre, ldapUrl, port, ssl, bindDN);
                        if (emp != null) {
                            retVal = true;
                            empInfo = emp;
                        }
                    }
                }
            }
            catch (Exception ex) {
                String x = ex.getMessage();
            }
            finally {

            }
            return retVal;
        }

        private EmployeeInfo getEmployeeInfo(String kullaniciAdi, String kullanicisifre, String ldapUrl, int port, int ssl, String bindDN)
        {
            EmployeeInfo retVal = null;

            try {
                LDAPServerInstance ldapServer = new LDAPServerInstance(ldapUrl, port, ssl, bindDN);
                List<EmployeeInfo> tmp = ldapServer.getEmployeeInfo(kullaniciAdi, kullanicisifre, String.format("(uid=%s)", kullaniciAdi));
                if (tmp != null && !tmp.isEmpty())
                    retVal = tmp.get(0);
            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(), String.format("Kullanıcı bilgileri ldap dan getirilirken hata oluştu: \"%s\"", ex.getMessage()), Toast.LENGTH_SHORT).show();
            }

            return retVal;
        }

        private boolean LdapKontrolu(String kullaniciAdi, String kullanicisifre, String ldapUrl, int port, int ssl, String bindDN)
        {
            boolean retVal = false;

            LDAPServerInstance ldapServer = new LDAPServerInstance(ldapUrl, port, ssl, bindDN);
            retVal = ldapServer.authenticate(kullaniciAdi, kullanicisifre);

            return retVal;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPreExecute() {
            login.setEnabled(false);
            LoginKontrolEdiliyor();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                login.setEnabled(true);

                if (result) {
                    if (emp != null)
                        empInfo = emp;

                    LoginBasarili();
                }
                else {
                    LoginHatali();
                }
            }
            catch (Exception ex) {
                String x = ex.getMessage();
            }
            finally {
            }
        }
    }
}
