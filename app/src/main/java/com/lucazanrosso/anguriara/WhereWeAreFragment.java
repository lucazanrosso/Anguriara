package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhereWeAreFragment extends Fragment {

    private GoogleMap mGoogleMap;
    LatLng latLng;
    float zoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.where_we_are));

        View view = inflater.inflate(R.layout.fragment_where_we_are, container, false);

        if (savedInstanceState == null) {
            latLng = new LatLng(45.7053472, 11.393271);
            zoom = 12.5f;
        } else {
            latLng = new LatLng(savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("lng"));
            zoom = savedInstanceState.getFloat("zoom");
        }
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(45.7053472, 11.393271)).title("Anguriara"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.my_location_map_anchor, mMapFragment).addToBackStack("secondary").commit();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("lat", mGoogleMap.getCameraPosition().target.latitude);
        outState.putDouble("lng", mGoogleMap.getCameraPosition().target.longitude);
        outState.putFloat("zoom", mGoogleMap.getCameraPosition().zoom);
    }
}