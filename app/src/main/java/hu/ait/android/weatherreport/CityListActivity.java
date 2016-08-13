package hu.ait.android.weatherreport;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import hu.ait.android.weatherreport.adapter.CityAdapter;
import hu.ait.android.weatherreport.adapter.CityTouchHelperCallback;
import hu.ait.android.weatherreport.data.City;
import hu.ait.android.weatherreport.fragment.AddCityFragment;

public class CityListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddCityFragment.OnCompleteListener {

    CityAdapter cityRecyclerAdapter;
    static public Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        cityRecyclerAdapter = new CityAdapter(this);
        final RecyclerView recyclerView =
                (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // RecyclerView layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cityRecyclerAdapter);

        ItemTouchHelper.Callback callback = new CityTouchHelperCallback(cityRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // API calls to update all cities weather images
                btnUpdate.setBackgroundColor(Color.TRANSPARENT);
                btnUpdate.setText(R.string.updatingString);
                // reset button format when all items are updated
                cityRecyclerAdapter.updateAllCities();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call add city dialog function
                showAddCityDialogFragment();

            }
        });

        cityRecyclerAdapter.updateAllCities();
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navAddCity) {
            showAddCityDialogFragment();
        } else if (id == R.id.navClearCities) {
            cityRecyclerAdapter.removeAllCities();
        } else if (id == R.id.navAbout) {
            Toast.makeText(CityListActivity.this, R.string.aboutMeString, Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAddCityDialogFragment() {
        DialogFragment dialog = new AddCityFragment();
        dialog.show(getFragmentManager(), AddCityFragment.TAG);
    }

    public void onComplete(String cityName) {
        City newCity = new City(cityName);
        cityRecyclerAdapter.addCity(newCity);
    }
}
