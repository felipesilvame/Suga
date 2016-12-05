package cl.usm.telematica.sigamobile;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String mcookie;
    private String location;
    public HttpURLConnection urlConnection = null;
    private LoginActivity parent;
    public static java.net.CookieManager msCookieManager;
    Toolbar toolbar;
    private String tipo;
    private String rutAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mcookie = extras.getString("Cookie", "");
            location = extras.getString("Location", "");
        }
        msCookieManager = LoginActivity.msCookieManager;
        URL welcome_url = null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("Menu Principal");

        //se para como argumento el linearlayout principal
        //LinearLayout navHeader = (LinearLayout) findViewById(R.id.linearLayoutHeader);
        new HeaderMainMenuHandler(this).execute();
        new PhotoMainHandler(this).execute();
        NewsFragment newsFragment = new NewsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_layout, newsFragment, newsFragment.getTag()).commit();
        setActionBarTitle("Noticias");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food) {
            LunchFragment foodFragment = new LunchFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_layout, foodFragment, foodFragment.getTag()).commit();
            setActionBarTitle("Almuerzos");
        }else if (id == R.id.nav_login){
            finish();
        }else if (id == R.id.nav_personal){
            PeronalFileFragment peronalFileFragment = new PeronalFileFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_layout, peronalFileFragment, peronalFileFragment.getTag()).commit();
            setActionBarTitle("Ficha Personal");
        } else if (id == R.id.nav_adv){
            ProgressFragment progressFragment = new ProgressFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_layout, progressFragment, progressFragment.getTag()).commit();
            setActionBarTitle("Avance Curricular");
        } else if (id == R.id.nav_resume){
            if (tipo != null && rutAlumno !=null){
                //TODO: make the posibility to download the resume.
            }else{
                Toast.makeText(this, "Un momento por favor...", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.nav_home){
            NewsFragment newsFragment = new NewsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_layout, newsFragment, newsFragment.getTag()).commit();
            setActionBarTitle("Noticias");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getCookie() {
        return mcookie;
    }

    public String getLocation() {
        return location;
    }

    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRutAlumno() {
        return rutAlumno;
    }

    public void setRutAlumno(String rutAlumno) {
        this.rutAlumno = rutAlumno;
    }
}
