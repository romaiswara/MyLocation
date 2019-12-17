package tugasptm.android.mylocation.view;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.model.LocationDao;
import tugasptm.android.mylocation.model.LocationDb;

public class NowFragment extends Fragment {

    Button btnSave,btnLoad, btnClear;

    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    GoogleMap map;
    SupportMapFragment mapFragment;
    String nama, kota;
    double lat, lng;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_now, container, false);

        initMap();
        getCurrentLocation();

        btnSave = root.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanLokasi();
            }
        });
        btnLoad = root.findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add remark
                loadMarker();
            }
        });
        btnClear = root.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hapus remark
                clearMarker();
            }
        });
        return root;
    }

    private void initMap(){

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                getCurrentLocation();
            }
        });
    }

    private void loadMarker(){
        map.clear();
        List<LocationDao> list = LocationDb.getDatabase(getActivity()).interfaceDao().getAllLocation();
        for (int i = 0; i < list.size(); i++) {
            LatLng latLngData = new LatLng(list.get(i).getLat(), list.get(i).getLng());
            map.addMarker(new MarkerOptions().position(latLngData).title(list.get(i).getKota()));
        }
    }

    private void clearMarker(){
        map.clear();
    }

    private void simpanLokasi(){
        LocationDao dao = new LocationDao(nama, kota, lat, lng);
        LocationDb.getDatabase(getActivity()).interfaceDao().insertLocation(dao);
        Toast.makeText(getActivity(), "Simpan Lokasi Berhasil : "+LocationDb.getDatabase(getActivity()).interfaceDao().getAllLocation().size(), Toast.LENGTH_SHORT).show();
    }


    private void getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        try {
                            Location currentLocation = (Location) task.getResult();
                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                            nama = getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
                            kota = getCity(currentLocation.getLatitude(), currentLocation.getLongitude());
                            lat = currentLocation.getLatitude();
                            lng = currentLocation.getLongitude();

                        } catch (NullPointerException e){

                        }
                    } else {
                        Toast.makeText(getActivity(), "Current Location is Null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e){
            Toast.makeText(getActivity(), "Security Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getCity(double lat, double lng){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat,lng,1);
            Log.d("latlng", "getCity: "+list.get(0).getAddressLine(0)+" --- "+list.get(0).getCountryName()+" --- "+list.get(0).getAdminArea()+" --- "+list.get(0).getLocality());
            Log.d("latlng", "getCity: "+list.get(0).getFeatureName()+" --- "+list.get(0).getSubLocality()+" --- "+list.get(0).getSubAdminArea()+" --- "+list.get(0).getCountryCode());
            return list.get(0).getSubAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getAddress(double lat, double lng){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat,lng,1);
            return list.get(0).getAddressLine(0).split(",")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}