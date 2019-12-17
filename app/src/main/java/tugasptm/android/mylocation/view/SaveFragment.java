package tugasptm.android.mylocation.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.adapter.LocationAdapter;
import tugasptm.android.mylocation.model.LocationDao;
import tugasptm.android.mylocation.model.LocationDb;

public class SaveFragment extends Fragment {
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    LocationAdapter adapter;
    List<LocationDao> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_save, container, false);

        list = LocationDb.getDatabase(getActivity()).interfaceDao().getAllLocation();
        adapter = new LocationAdapter(getActivity(), list, new LocationAdapter.itemClickListener() {
            @Override
            public void onItemClick(LocationDao locationDao, boolean isLongClick) {
                if (isLongClick){
                    // hapus lokasi
                    deleteDialog(getActivity(), locationDao.getId());
                } else {
                    // detail dari lokasi
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", locationDao.getId());
                    Navigation.findNavController(getView()).navigate(R.id.action_nav_save_to_nav_detail, bundle);
                }
            }
        });

        refresh = root.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh.isRefreshing()){
                            refresh.setRefreshing(false);
                        }
                        adapter.updateData(LocationDb.getDatabase(getActivity()).interfaceDao().getAllLocation());
                    }
                }, 1000);
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
                        LocationDb.getDatabase(context).interfaceDao().deleteLocationById(id);
                        adapter.updateData(LocationDb.getDatabase(getActivity()).interfaceDao().getAllLocation());
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
}