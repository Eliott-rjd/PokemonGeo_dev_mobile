package com.example.pokemongeo.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo.PokemonViewModel;
import com.example.pokemongeo.R;
import com.example.pokemongeo.database.Database;
import com.example.pokemongeo.databinding.MapFragmentBinding;
import com.example.pokemongeo.fight.MapToFightListener;
import com.example.pokemongeo.fight.FightFragment;
import com.example.pokemongeo.model.Pokemon;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Random;

public class MapFragment extends Fragment {

    MyLocationNewOverlay mLocationOverlay;
    MapView mapView;
    int userIcon = R.drawable.trainer;
    Marker markerTrainer;

    private Database mydb;

    Location userLocation;

    MapFragmentBinding binding;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment,container,false);
        context = binding.getRoot().getContext();
        Configuration.getInstance().load(context,PreferenceManager.getDefaultSharedPreferences(context));

        // SETUP MAP
        mapView = binding.mapView;
        mapView.setTileSource( TileSourceFactory.MAPNIK ); //source des donn´ees cartographique
        mapView.getController().setZoom(17.0);
        mapView.setMinZoomLevel(16.0);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),binding.mapView);

        mydb = Database.getInstance(binding.getRoot().getContext());

        return binding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView = binding.mapView;
        setLocation(userLocation); // Actualise position on the trainer
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView = null;
    }

    public void setLocation(Location location) {
        if(mapView != null ) {
            // POKEMON :
            createPokemons();

            // STRUCKTURE
            Thread mapAPIManager = new MapAPIManager(new PokestopListener() {
                @Override
                public void setupPokestop(GeoPoint geoPoint) {
                    Marker marker = addMarker(geoPoint, R.drawable.pokestop);
                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker, MapView mapView) {
                            System.out.println("Somthing is happening");
                            // TODO : add stuff to inventory
                            // TODO : heal pokemon life ?
                            return false;
                        }
                    });
                    // TODO set comportement marker
                }

            });
            mapAPIManager.start();

            if (markerTrainer != null) {
                mapView.getOverlays().remove(markerTrainer);
            }
            markerTrainer = addMarker(new GeoPoint( userLocation.getLatitude(), userLocation.getLongitude() ), userIcon);
            markerTrainer.setOnMarkerClickListener((marker, mapView) -> false); // marker uncklicable
            GeoPoint userPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapView.getController().setCenter(userPosition);
        }else {
            userLocation = location;
        }
    }

    /**
     * Add marker on the map
     * @param geoPoint
     * @param icon
     * @return
     */
    private Marker addMarker(GeoPoint geoPoint, int icon){
        if(mapView != null) {
            Marker marker = new Marker(mapView);

            marker.setPosition(geoPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            marker.setIcon(getResources().getDrawable(icon));

            mapView.getOverlays().add(marker);
            return marker;
        }
        return null;
    }

    /**
     * Generate randoms pokemons and add them on the map as marker using addMarker.
     * Pokemons are generated around the trainer
     */
    private void createPokemons(){
        if(mapView != null) {
            Random r = new Random();
            int low = 1;
            int high = 152;

            double rayonMax = 0.013;

            Location newPokemonLocation;
            int drawableID;

            for (int counter = 0; counter <= 5; counter++){
                int dexNo = r.nextInt(high-low) + low;

                // Location management :
                GeoPoint newPokemonGeoPoint = new GeoPoint( userLocation.getLatitude() + rayonMax/2 - r.nextFloat() *rayonMax
                        , userLocation.getLongitude() +rayonMax/2 - r.nextFloat() *rayonMax
                );


                // Drawable management :
                drawableID = context.getResources().getIdentifier("p" + dexNo, "drawable",
                        context.getPackageName());

                // Marker management :
                Marker newPokemon = addMarker( newPokemonGeoPoint, drawableID );

                newPokemon.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        PokemonViewModel opponent = new PokemonViewModel();
                        opponent.setPokemon( mydb.getOnePokemonByDexNo(dexNo) );

                        PokemonViewModel player = new PokemonViewModel();
                        // TODO : better selection of the pokemon that will fight
                        Pokemon selectedPlayerPokemon = mydb.getMyPokemon().get(0);
                        player.setPokemon( selectedPlayerPokemon );

                        //TODO virer la nav bar pdt le combat

                        FightFragment fightFragment = new FightFragment(opponent, player);

                        fightFragment.setListener(
                                new MapToFightListener() {
                                    @Override
                                    public void goBackToMap() {
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(fightFragment).commit();
                                    }

                                    @Override
                                    public void removeMarker() {
                                        marker.remove(mapView);
                                    }
                                }
                        );
                        getActivity().getSupportFragmentManager().beginTransaction().add( R.id.fragment_container, fightFragment).commit();
                        return false;
                    }
                });
            }
        }
    }

}