package com.evilflora.warframesentinel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static WarframeWorldState warframeWorldState;
    AppSettings settings;
    Activity activity;
    Handler hReloadWarframeWorldState = new Handler();
    android.support.v4.app.Fragment currentFrag =  new EventFragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
        settings = new AppSettings(activity);

        Intent myService = new Intent(this, NotificationServiceClass.class);
        if (settings.get_activate_notification()) {
            startService(myService);
        } else {
           stopService(myService);
        }

        warframeWorldState = new WarframeWorldState(settings.get_platform_code());

        hReloadWarframeWorldState.post(runnableReloadWarframeWorldState); // On rafraichis toutes les secondes les timers

        // disallowAddToBackStack empêche le bouton retour de revenir avant l'execution de cette ligne où le fragment ne contient pas de vue
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, currentFrag).disallowAddToBackStack().commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Runnable runnableReloadWarframeWorldState = new Runnable() {
        @Override
        public void run() {
            try {
                settings = new AppSettings(activity);
                warframeWorldState.ReloadWarframeWorldSate(settings.get_platform_code());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hReloadWarframeWorldState.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingFragment()).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_news:
                currentFrag = new NewsFragment();
                break;
            case R.id.nav_alerts:
                currentFrag = new EventFragment();
                break;
            case R.id.nav_invasions:
                currentFrag = new InvasionFragment();
                break;
            case R.id.nav_earth:
                currentFrag = new BountiesFragment();
                break;
            case R.id.nav_darvo:
                currentFrag = new DarvoMarketFragment();
                break;
            case R.id.nav_traders:
                currentFrag = new VoidTraderFragment();
                break;
            case R.id.nav_sorties:
                currentFrag = new SortieFragment();
                break;
            case R.id.nav_fissures:
                currentFrag = new FissureFragment();
                break;
            case R.id.nav_syndicate:
                currentFrag = new SyndicateFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, currentFrag).addToBackStack(null).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
