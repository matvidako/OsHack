package hr.ms.oshack;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpMapIfNeeded();
        setupGoogleServices();
    }

    private void setupGoogleServices() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            return;
        }

        LatLng userPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraPosition center = new CameraPosition.Builder().zoom(10).target(userPos).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(center);
        mMap.moveCamera(update);
        addHeatMap();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void addHeatMap() {
        List<LatLng> list = new ArrayList<>();
        Random random = new Random();

        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        double limit = 10;

        for(int i = 0; i < 50; i++) {
            list.add(new LatLng(latitude+random.nextDouble(), longitude+random.nextDouble()));
        }

        for(int i = 0; i < 500; i++) {
            list.add(new LatLng(latitude+random.nextDouble()/limit, longitude+random.nextDouble()/limit));
        }

        //Gradient gradient = new Gradient(new int[]{Color.rgb(0, 255, 0), Color.rgb(255, 0, 0)}, new float[]{0f, 1f});
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                //.gradient(gradient)
                .build();
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

}
