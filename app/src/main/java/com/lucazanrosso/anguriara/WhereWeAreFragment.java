package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        MainActivity.toolbar.setTitle(getResources().getString(R.string.where_we_are));
        MainActivity.toolbar.setLogo(null);

        View view = inflater.inflate(R.layout.fragment_where_we_are, container, false);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                LatLng anguriaraLatLng = new LatLng(45.7053472, 11.393271);
                mGoogleMap.addMarker(new MarkerOptions().position(anguriaraLatLng).title("Anguriara"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(anguriaraLatLng, 15));
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.my_location_map_anchor, mMapFragment).addToBackStack("secondary").commit();

        return view;
    }
}