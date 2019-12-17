package tugasptm.android.mylocation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.model.LocationDao;
import tugasptm.android.mylocation.model.LocationDb;

public class DetailFragment extends Fragment {
    LocationDao locationDao;
    int id;
    SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        id = getArguments().getInt("id", 0);
        locationDao = LocationDb.getDatabase(getActivity()).interfaceDao().getLocationById(id);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(locationDao.getLat(), locationDao.getLng());
                googleMap.addMarker(new MarkerOptions().position(latLng).title(locationDao.getKota()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });

        return root;
    }
}