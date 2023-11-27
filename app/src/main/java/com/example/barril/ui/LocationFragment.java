package com.example.barril.ui;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.barril.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Agregar un clic al fragmento para mostrar un Toast

                showToast("Fragment clicked!");


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.icono_maps);

        // Marcador en la Puerta del Sol, Madrid
        LatLng puertaDelSol = new LatLng(40.4169, -3.7038);
        mMap.addMarker(new MarkerOptions().position(puertaDelSol).title("Puerta del Sol, Madrid").icon(icono));

        // Marcador en el Parque del Retiro, Madrid
        LatLng retiroPark = new LatLng(40.4150, -3.6842);
        mMap.addMarker(new MarkerOptions().position(retiroPark).title("Parque del Retiro, Madrid").icon(icono));

        // Marcador en la Plaza Mayor, Madrid
        LatLng plazaMayor = new LatLng(40.4155, -3.7074);
        mMap.addMarker(new MarkerOptions().position(plazaMayor).title("Plaza Mayor, Madrid").icon(icono));

        // Marcador en Gran Vía, Madrid
        LatLng granVia = new LatLng(40.4181, -3.7019);
        mMap.addMarker(new MarkerOptions().position(granVia).title("Gran Vía, Madrid").icon(icono));

        // Marcador en el Templo de Debod, Madrid
        LatLng temploDeDebod = new LatLng(40.4114, -3.6932);
        mMap.addMarker(new MarkerOptions().position(temploDeDebod).title("Templo de Debod, Madrid").icon(icono));

        // Marcador en el Museo del Prado, Madrid
        LatLng museoDelPrado = new LatLng(40.4040, -3.6965);
        mMap.addMarker(new MarkerOptions().position(museoDelPrado).title("Museo del Prado, Madrid").icon(icono));

        // Marcador en Malasaña, Madrid
        LatLng malasana = new LatLng(40.4194, -3.6923);
        mMap.addMarker(new MarkerOptions().position(malasana).title("Malasaña, Madrid").icon(icono));

        // Marcador en Tribunal, Madrid
        LatLng tribunal = new LatLng(40.4264, -3.7034);
        mMap.addMarker(new MarkerOptions().position(tribunal).title("Tribunal, Madrid").icon(icono));

        // Marcador en Chamartín, Madrid
        LatLng chamartin = new LatLng(40.4356, -3.6907);
        mMap.addMarker(new MarkerOptions().position(chamartin).title("Chamartín, Madrid").icon(icono));

        // Marcador en el Estadio Santiago Bernabéu, Madrid
        LatLng santiagoBernabeu = new LatLng(40.4397, -3.7131);
        mMap.addMarker(new MarkerOptions().position(santiagoBernabeu).title("Estadio Santiago Bernabéu, Madrid").icon(icono).snippet("Descripción detallada de Parque del Retiro"));



        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Obtener la información del marcador
                String title = marker.getTitle();
                String snippet = marker.getSnippet();

                // Abrir la aplicación de mapas con indicaciones
                if (title != null && !title.isEmpty() && snippet != null && !snippet.isEmpty()) {
                    String uri = "google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&mode=d";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }

                return false;
            }
        });*/

        // Mover la cámara a la ubicación del primer lugar (Puerta del Sol)
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puertaDelSol, 15));


        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        }
                    }
                });
    }





    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
