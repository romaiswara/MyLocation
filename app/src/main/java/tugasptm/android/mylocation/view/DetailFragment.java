package tugasptm.android.mylocation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.model.Location;
import tugasptm.android.mylocation.model.response.LocationResponse;
import tugasptm.android.mylocation.network.ApiClient;
import tugasptm.android.mylocation.network.LokasiService;

public class DetailFragment extends Fragment {
    Location location;
    int id;
    SupportMapFragment mapFragment;
    LokasiService service;
    GoogleMap map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        service = ApiClient.getClient().create(LokasiService.class);
        id = getArguments().getInt("id", 0);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
        readLokasi(id);

        return root;
    }

    private void readLokasi (int id){
        Call<LocationResponse> call = service.read(id);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isSuccess()){
                        location = response.body().getResult().get(0);
                        LatLng latLng = new LatLng(location.getLat(), location.getLng());
                        map.addMarker(new MarkerOptions().position(latLng).title(location.getKota()));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    } else {
                        Toast.makeText(getActivity(), "Gagal Load Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}