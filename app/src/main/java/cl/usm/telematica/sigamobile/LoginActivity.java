package cl.usm.telematica.sigamobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.CookieManager;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AppCompatCheckBox mcheckbox;
    private Spinner mRadioGroup;
    List<User> usuarios;
    List<String> usuarios_nombres;
    private String cookie="";
    private HttpURLConnection urlConnection;
    static final String COOKIES_HEADER = "Set-Cookie";
    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserTable db = new UserTable(getApplicationContext());
        usuarios = db.getAllUsers();


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mcheckbox = (AppCompatCheckBox) findViewById(R.id.checkbox_1);
        mRadioGroup = (Spinner) findViewById(R.id.serverRealmSpinner);

        usuarios_nombres = new ArrayList<String>();
        if (!usuarios.isEmpty()) {
                for (User e : usuarios) {
                    usuarios_nombres.add(e.getUsername());
                }

        }
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.autoLoginSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, usuarios_nombres);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button autoSignIn = (Button) findViewById(R.id.autoSignIn);
        autoSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAutoLogin();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(LoginActivity.this,CredentialsActivity.class);
                startActivity(i);
                return true;
            case R.id.info_siga:
                Intent info_siga = new Intent(LoginActivity.this, WebViewInfoSigaActivity.class);
                startActivity(info_siga);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_layout, menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptAutoLogin() {
        if (mAuthTask != null) {
            return;
        }
        if (usuarios_nombres.isEmpty()){
            return;
        }
        Spinner autoSelector = (Spinner) findViewById(R.id.autoLoginSpinner);
        int position = autoSelector.getSelectedItemPosition();
        User usuario = usuarios.get(position);

        String servidor = usuario.getServer();

        // Store values at the time of the login attempt.
        String username = usuario.getUsername();
        String password = usuario.getPass();

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password, servidor);
            mAuthTask.execute((Void) null);
        }
    }
    public HttpURLConnection getUrlConnection(){
        return urlConnection;
    }
    public String getCookie(){
        return cookie;
    }
    public void setCookie(String mcookie){
        cookie = mcookie;
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        String servidor = "";
        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        int server_id = mRadioGroup.getSelectedItemPosition();
        switch (server_id){
            //@alumnos.usm.cl
            case 0:
                servidor = "alumnos.usm.cl";
                break;
            case 1:
                servidor = "sansano.usm.cl";
                break;
            case 2:
                servidor = "titulados.usm.cl";
                break;
            case 3:
                servidor = "postgrado.usm.cl";
                break;
            case 4:
                servidor = "usm.cl";
                break;
            default:
                servidor = "sansano.usm.cl";
                break;
        }
        mcheckbox = (AppCompatCheckBox)findViewById(R.id.checkbox_1);
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password)) {
//            mPasswordView.setError(getString(R.string.error_incorrect_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (mcheckbox.isChecked()){
                UserTable db = new UserTable(getApplicationContext());
                db.addUser(new User(username,password,servidor));
            }
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password, servidor);
            mAuthTask.execute((Void) null);
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mServidor;
        private String mCookie;
        private  String location;

        UserLoginTask(String email, String password,String servidor) {
            mEmail = email;
            mPassword = password;
            mServidor = servidor;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                URL login_url = new URL("https://siga.usm.cl//pag/valida_login.jsp");
                urlConnection = (HttpURLConnection) login_url.openConnection();
                urlConnection.setUseCaches( true );
                urlConnection.setInstanceFollowRedirects(false);
                String urlParamameters = "";
                urlParamameters += "login="+mEmail;
                urlParamameters += "&server="+mServidor;
                urlParamameters += "&passwd="+mPassword;
                byte[] postData = urlParamameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                urlConnection.setDoOutput( true );
                urlConnection.setRequestMethod( "POST" );
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "charset", "utf-8");
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    wr.write( postData );
                }
                int response = urlConnection.getResponseCode();
                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

//
//                int response = urlConnection.getResponseCode();
//                if (response == HttpURLConnection.HTTP_OK){
//
//
////                    for (Map.Entry<String, List<String>> header : urlConnection.getHeaderFields().entrySet()) {
////                        if (header.getKey().equals("Set-Cookie")){
////
////                        }else if (header.getKey().equals("Location")){
////                            location = header.getValue().toString();
////                        }
////                        Log.v(header.getKey(), header.getValue().toString());
////                    }
//                    String server_response = readStream(urlConnection.getInputStream());
//                    //Log.v("CatalogClient", server_response);
//                    //urlConnection.disconnect();
//                    mCookie= response.header("Set-Cookie");
//                    String[] tokens = mCookie.split(";");
 //                   mCookie = tokens[0];
//                    setCookie(mCookie);
//
//                    //Log.v("Cookie", cookie);
//
                    location = urlConnection.getHeaderField("Location");
//                    //Log.v("Location", location);
//                    //cambiar por true para finalizar
//
                    if (location.contains("error_ingreso_login.jsp")){
                        return false;
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("Cookie",mCookie);
                        bundle.putString("Location", location);
                        Intent logged_in = new Intent(LoginActivity.this, MainMenuActivity.class);
                        logged_in.putExtras(bundle);
                        startActivity(logged_in);
                        return true;
                    }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //urlConnection.disconnect();
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                mEmailView.setText("");
                mPasswordView.setText("");
                urlConnection.disconnect();
                //finish();
            } else {
                Toast.makeText(LoginActivity.this, "Error ingresando al SIGA", Toast.LENGTH_SHORT).show();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                urlConnection.disconnect();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            urlConnection.disconnect();
        }
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }
}

