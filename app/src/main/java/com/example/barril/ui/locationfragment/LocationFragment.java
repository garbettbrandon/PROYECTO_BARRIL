package com.example.barril.ui.locationfragment;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.barril.R;
import com.example.barril.ui.admin.AgregarLugarAdmin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    ImageView masMapasAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        masMapasAdmin = rootView.findViewById(R.id.masMapasAdmin);
        masMapasAdmin.setVisibility(View.INVISIBLE);
        comprobarAdmin(recogerUserId());

        masMapasAdmin.setOnClickListener(view ->{
            Intent intent = new Intent(getContext(), AgregarLugarAdmin.class);
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.icono_maps);

       /* // PARA PONER PUNTOS EN EL MAPA DE UNO EN UNO
        LatLng puertaDelSol = new LatLng(40.4169, -3.7038);
        mMap.addMarker(new MarkerOptions().position(puertaDelSol).title("Puerta del Sol, Madrid").icon(icono));*/


        //recoger de firebase
        recogerDeMapa(mMap, icono);


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

    public void recogerDeMapa(GoogleMap mMap, BitmapDescriptor icono){


        // Recuperar puntos de la base de datos y agregar marcadores
        db.collection("mapas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    // Obtener datos del documento
                    String titulo = document.getString("titulo");
                    String latitud = document.getString("latitud");
                    String longitud = document.getString("longitud");
                    String descripcion = document.getString("descripcion");

                    Double latitudADouble = Double.parseDouble(latitud);
                    Double longitudADouble = Double.parseDouble(longitud);

                    // Agregar marcador al mapa
                    if (titulo != null && descripcion != null) {
                        LatLng lugar = new LatLng(latitudADouble, longitudADouble);
                        mMap.addMarker(new MarkerOptions().position(lugar).title(titulo).snippet(descripcion).icon(icono));
                    }
                }
            } else {
                // Manejar errores al obtener puntos de la base de datos
                showToast("Error al obtener puntos del mapa");
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public  String recogerUserId(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
    private void comprobarAdmin(String userId){
        db.collection("usuarios").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //comprobar el campo "admin"
                        boolean admin = document.contains("admin") ? document.getBoolean("admin") : false;
                        ocultarBoton(admin);
                    }
                }
            }
        });
    }
    private void ocultarBoton(boolean admin){
        if(admin == true){
            masMapasAdmin.setVisibility(View.VISIBLE);
        }
    }
}
