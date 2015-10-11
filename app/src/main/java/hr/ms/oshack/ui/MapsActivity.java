package hr.ms.oshack.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import hr.ms.oshack.R;
import hr.ms.oshack.model.Bite;
import hr.ms.oshack.model.Bites;
import hr.ms.oshack.model.Cluster;
import hr.ms.oshack.model.Trap;
import hr.ms.oshack.model.Traps;
import hr.ms.oshack.net.Mosquito;
import hr.ms.oshack.storage.PrefsManager;
import hr.ms.oshack.ui.tutorial.TutorialActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends MenuActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    public static String EXTRA_ADD_TRAP_ON_START = "ExtraAddTrap";

    private GoogleMap map;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;

    private boolean shouldAddTrapOnStart = false;
    private Map<String, Trap> trapMarkerHashMap = new HashMap<>();
    private List<Marker> trapMarkers = new ArrayList<>();
    private List<Marker> clusterMarkers = new ArrayList<>();
    private List<Circle> clusterCircles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldAddTrapOnStart = getIntent().getBooleanExtra(EXTRA_ADD_TRAP_ON_START, false);
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

        loadClusters();
        loadTraps();
    }

    private void loadTraps() {
        trapMarkerHashMap.clear();
        Mosquito.getInstance().getTraps(new Callback<Traps>() {
            @Override
            public void success(final Traps traps, Response response) {
                for (Marker trapMarker : trapMarkers) {
                    trapMarker.remove();
                }
                addTraps(traps);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void loadClusters() {
        Mosquito.getInstance().getClusters(new Callback<List<Cluster>>() {
            @Override
            public void success(List<Cluster> clusters, Response response) {
                for (Marker clusterMarker : clusterMarkers) {
                    clusterMarker.remove();
                }
                for (Circle clusterCircle : clusterCircles) {
                    clusterCircle.remove();
                }
                addClusters(clusters);
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
                loadClusters();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("DISI", "" + error);
            }
        });
    }

    @OnClick(R.id.fabTrap)
    public void onAddTrapClick() {
        if(shouldShowTrapTutorialDialog()) {
            showTrapTutorialDialog();
        } else {
            addTrap();
        }
    }

    private void addTrap() {
        Toast.makeText(this, getString(R.string.trap_success), Toast.LENGTH_SHORT).show();
        Mosquito.getInstance().addTrap(Trap.fromLocation(lastLocation), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d("DISI", "" + response);
                loadTraps();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("DISI", "" + error);
            }
        });
    }

    private boolean shouldShowTrapTutorialDialog() {
        return !PrefsManager.didSeeTrapTutorial(this);
    }

    private void showTrapTutorialDialog() {
        PrefsManager.onTrapTutorialSeen(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.trap_tutorial_dialog_title);
        builder.setMessage(R.string.trap_tutorial_dialog_message);
        builder.setPositiveButton(R.string.trap_tutorial_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MapsActivity.this, TutorialActivity.class));
            }
        });
        builder.setNegativeButton(R.string.trap_tutorial_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addTrap();
            }
        });
        builder.show();
    }

    private void refreshMap() {
        map.clear();
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

            Circle circle = map.addCircle(circleOptions);
            clusterCircles.add(circle);

            addClusterMarker(cluster);

        }
    }

    private void addTraps(Traps traps) {
        for (Trap trap : traps.traps) {
            int iconId = trap.isActive() ? R.drawable.pin_zamka : R.drawable.pin_zamka_0;
            Marker marker = addCircleMarker(trap.latitude, trap.longitude, iconId);
            trapMarkerHashMap.put(marker.getId(), trap);
            trapMarkers.add(marker);
            if (trap.isActive()) {
                marker.setTitle("Prijavi neispravnu zamku");
            }
            else {
                marker.setTitle("Prijavi popravljenu zamku");
            }
        }
    }

    private void addClusterMarker(Cluster cluster) {
        Marker marker = addCircleMarker(cluster.latitude, cluster.longitude, R.drawable.pin_ubod);
        clusterMarkers.add(marker);
        marker.setTitle(Integer.toString(cluster.number));
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
        if(shouldAddTrapOnStart) {
            addTrap();
            shouldAddTrapOnStart = false;
        } else {
        loadData();
    }
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
    public void onInfoWindowClick(final Marker marker) {
        Trap trap = trapMarkerHashMap.get(marker.getId());
        if (trap == null) {
            return;
        }
        marker.hideInfoWindow();
        Mosquito.getInstance().toggleTrapState(trap,  new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                loadTraps();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
