package co.il.liam.booktobook;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private List<String> locations;
    private String userLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(this);

        //getting the locations from the activity
        locations = getArguments().getStringArrayList("locations");
        userLocation = getArguments().getString("userLocation");

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        Geocoder geocoder = new Geocoder(getContext());

        //adding user location
        try {
            List<Address> userAddress = geocoder.getFromLocationName(userLocation, 1);
            if (!userAddress.isEmpty()) {
                Address address = userAddress.get(0);
                LatLng userLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                //adding user location
                MarkerOptions userMarker = new MarkerOptions()
                        .position(userLatLng)
                        .title("Your location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                gMap.addMarker(userMarker);

                //moving the camera to the users location
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15.0f));
            }
            else {
                Log.d("qqq", "Failed to add user location");
            }
        }
        catch (IOException e) {
            Log.d("qqq", "Failed to get user location LatLng");
        }


        //adding other locations
        for (String location : locations) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng addressLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                    //adding each location
                    MarkerOptions addressMarker = new MarkerOptions()
                            .position(addressLatLng)
                            .title("Book location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    gMap.addMarker(addressMarker);
                }
                else {
                    Log.d("qqq", "Failed to add a book location");
                }
            }
            catch (IOException e) {
                Log.d("qqq", "Failed to get book location LatLng");
            }
        }


    }
}