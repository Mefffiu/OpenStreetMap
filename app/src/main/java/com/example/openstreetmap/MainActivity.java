package com.example.openstreetmap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;


public class MainActivity extends Activity implements MapEventsReceiver{
    MapView map;
    MapController mapController;
    ArrayList<GeoPoint> pointList = new ArrayList<>();
    RoadManager roadManager = new OSRMRoadManager(this);

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        mapController = (MapController) map.getController();

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setClickable(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(50.061389, 19.938333);
        mapController.setCenter(startPoint);
        mapController.setZoom(14);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);


    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        Toast.makeText(this, "Tap on ("+p.getLatitude()+","+p.getLongitude()+")", Toast.LENGTH_SHORT).show();
        pointList.add(p);

        Marker marker = new Marker(map);
        marker.setPosition(p);
        map.getOverlays().add(marker);
        map.invalidate();

        marker.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        marker.setTitle("Centered on "+p.getLatitude()+","+p.getLongitude());

//        if (pointList.size() > 1) {
//            Road road = roadManager.getRoad(pointList);
//            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//            map.getOverlays().add(roadOverlay);
//            map.invalidate();
//        }

        return true;
    }
}