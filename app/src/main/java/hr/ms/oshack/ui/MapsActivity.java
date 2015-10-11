package hr.ms.oshack.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.OnClick;
import hr.ms.oshack.R;
import hr.ms.oshack.model.Bite;
import hr.ms.oshack.model.Bites;
import hr.ms.oshack.model.Cluster;
import hr.ms.oshack.model.Trap;
import hr.ms.oshack.model.Traps;
import hr.ms.oshack.net.Mosquito;
import hr.ms.oshack.storage.PrefsManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends MenuActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;

    private Map<String, Trap> trapHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startOnboardingIfNotSeen();
        setupGoogleServices();
        setTitle(R.string.action_map);
    }

    private void startOnboardingIfNotSeen() {
        if(!PrefsManager.didSeeOnboarding(this)) {
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    private void loadData() {
//        Mosquito.getInstance().getBites(new Callback<Bites>() {
//            @Override
//            public void success(final Bites bites, Response response) {
//                addHeatMap(bites);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//            }
//        });

        Mosquito.getInstance().getClusters(new Callback<List<Cluster>>() {
            @Override
            public void success(List<Cluster> clusters, Response response) {
                addClusters(clusters);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        Mosquito.getInstance().getTraps(new Callback<Traps>() {
            @Override
            public void success(final Traps traps, Response response) {
                addTraps(traps);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_maps;
    }

    @Override
    protected int getMenuResId() {
        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    private void setUpMap() {
        if (map != null) {
            return;
        }
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setMyLocationEnabled(true);
        map.setOnInfoWindowClickListener(this);
    }

    private void setupGoogleServices() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @OnClick(R.id.fabBite)
    public void reportBite() {
        Mosquito.getInstance().reportBite(Bite.fromLocation(lastLocation), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d("DISI", "" + response);
                refreshMap();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("DISI", "" + error);
            }
        });
    }

    @OnClick(R.id.fabTrap)
    public void addTrap() {
        Mosquito.getInstance().addTrap(Trap.fromLocation(lastLocation), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d("DISI", "" + response);
                refreshMap();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("DISI", "" + error);
            }
        });
    }

    private void refreshMap() {
        map.clear();
        trapHashMap.clear();
        loadData();
    }

    private void addHeatMap(Bites bites) {
        List<LatLng> list = new ArrayList<>();

        for(Bite bite : bites.bites) {
            list.add(new LatLng(bite.latitude, bite.longitude));
        }

        //Gradient gradient = new Gradient(new int[]{Color.rgb(0, 255, 0), Color.rgb(255, 0, 0)}, new float[]{0f, 1f});
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                        //.gradient(gradient)
                .build();
        TileOverlay mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

    private void addClusters(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(cluster.latitude, cluster.longitude))
                    .radius(cluster.radius)
                    .strokeWidth(0)
                    .fillColor(getResources().getColor(R.color.red_region))
                    .zIndex(1);

            map.addCircle(circleOptions);

            addClusterMarker(cluster);

        }
    }

    private void addTraps(Traps traps) {
        for (Trap trap : traps.traps) {
            Marker marker = addCircleMarker(trap.latitude, trap.longitude, R.drawable.pin_zamka);
            trapHashMap.put(marker.getId(), trap);
            if (trap.isActive()) {
                marker.setTitle("Prijavi neispravnu zamku");
            }
            else {
                marker.setTitle("Prijavi popravljenu zamku");
            }
        }
    }

    private void addClusterMarker(Cluster cluster) {
        addCircleMarker(cluster.latitude, cluster.longitude, R.drawable.pin_ubod);
    }

    private Marker addCircleMarker(double latitude, double longitude, int iconId) {
        return map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(iconId))
                .anchor(0.5f, 0.5f));
    }

    private void centerMap() {
        LatLng userPos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        CameraPosition center = new CameraPosition.Builder().zoom(12).target(userPos).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(center);
        map.moveCamera(update);
    }

    private void onLocationReady() {
        centerMap();
        loadData();
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null) {
            return;
        }
        onLocationReady();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Trap trap = trapHashMap.get(marker.getId());
        Mosquito.getInstance().toggleTrapState(trap,  new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                refreshMap();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
