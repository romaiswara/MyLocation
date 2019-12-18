package tugasptm.android.mylocation.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.adapter.LocationAdapter;
import tugasptm.android.mylocation.model.Location;
import tugasptm.android.mylocation.model.response.GeneralResponse;
import tugasptm.android.mylocation.model.response.LocationResponse;
import tugasptm.android.mylocation.network.ApiClient;
import tugasptm.android.mylocation.network.LokasiService;

public class SaveFragment extends Fragment {
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    LocationAdapter adapter;
    List<Location> list;

    LokasiService service;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_save, container, false);

        service = ApiClient.getClient().create(LokasiService.class);

        list = new ArrayList<>();

        adapter = new LocationAdapter(getActivity(), list, new LocationAdapter.itemClickListener() {
            @Override
            public void onItemClick(Location location, boolean isLongClick) {
                if (isLongClick){
                    // hapus lokasi
                    deleteDialog(getActivity(), location.getId());
                } else {
                    // detail dari lokasi
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", location.getId());
                    Navigation.findNavController(getView()).navigate(R.id.action_nav_save_to_nav_detail, bundle);
                }
            }
        });

        // membaca data dari mysql
        readLokasi();

        refresh = root.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readLokasi();
            }
        });

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return root;
    }

    public void deleteDialog(final Context context, final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage("Delete location ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // menghapus data dari mysql
                        deleteLokasi(id);
                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void readLokasi (){
        Call<LocationResponse> call = service.read();
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isSuccess()){
                        adapter.updateData(response.body().getResult());
                    } else {
                        Toast.makeText(getActivity(), "Gagal Load Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }

                if (refresh.isRefreshing()){
                    refresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                if (refresh.isRefreshing()){
                    refresh.setRefreshing(false);
                }

            }
        });
    }

    private void deleteLokasi(int id){
        Call<GeneralResponse> call = service.delete(id);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()){
                    if  (response.body().isSuccess()){
                        readLokasi();
                    }
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}