package com.example.pokemongeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.pokemongeo.database.Database;
import com.example.pokemongeo.inventory.InventoryFragment;
import com.example.pokemongeo.map.MapFragment;
import com.example.pokemongeo.pokedex.OnClickOnNoteListener;
import com.example.pokemongeo.pokedex.PokedexFragment;
import com.example.pokemongeo.pokedex.PokemonDetailsFragment;
import com.example.pokemongeo.starter.StarterSelectionFragment;
import com.example.pokemongeo.starter.StarterSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.pokemongeo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;

    private BottomNavigationView bottomNavigationView;

    private Database mydb;

    PokedexFragment pokedexFragment;
    InventoryFragment inventoryFragment = new InventoryFragment();
    MapFragment mapFragment = new MapFragment();

    Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        askPermission();
        showStartup();

        mydb = Database.getInstance(binding.getRoot().getContext());
    }

    private void askPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            getPosition();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPosition();
        } else {
            System.out.println("Perm Not granted");
            //afficher un message d’erreur
        }
    }

    private void getPosition() {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // New listener
        android.location.LocationListener myLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLocation = location;
                System.out.println("Location changed : " + location);
                mapFragment.setLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider){
                // Au démarrage, onProviderDisabled est appelée une deuxième fois, à cause de l'activation
                // de la localisation via les données mobiles. Le message ici ne concerne que le GPS
                if (provider.equals("gps")) {
                    buildAlertMessageNoGps(); // ask the user to enable the location
                }
            }
            @Override
            public void onProviderEnabled(String provider){}
        };


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Start requesting loc
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120L, 10f, myLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120L, 10f, myLocationListener);
    }

    private void buildAlertMessageNoGps() {
        // FROM :https://stackoverflow.com/questions/25175522/how-to-enable-location-access-programmatically-in-android
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void showStartup() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // Creating listener
        OnClickOnNoteListener listener = new OnClickOnNoteListener(){
            @Override
            public void onClickOnNote(PokemonViewModel viewModel){
                showNoteDetail(viewModel);
            }
        };

        pokedexFragment = new PokedexFragment(listener);

        transaction.replace(R.id.fragment_container,pokedexFragment);
        transaction.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.pokedex);

    }


    public void showNoteDetail(PokemonViewModel viewModel) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        PokemonDetailsFragment fragment = new PokemonDetailsFragment(viewModel);

        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.pokedex) {
            replaceFragment(pokedexFragment);
            return true;
        }
        if (item.getItemId() == R.id.map) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                // Show alert message if user try access to the map without gps
                buildAlertMessageNoGps();
            } else if (mydb.getMyPokemon().isEmpty()) {
                // If no pkm then select a starter
                replaceFragment( new StarterSelectionFragment(new StarterSelectionListener() {
                    @Override
                    public void onSarterSelected() {
                        replaceFragment(mapFragment);
                    }
                    })
                );
            }else {
                replaceFragment(mapFragment);
            }

            return true;
        }
        if (item.getItemId() == R.id.inventaire) {
            replaceFragment(inventoryFragment);
            return true;
        }
        return false;
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}