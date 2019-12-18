package tugasptm.android.mylocation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tugasptm.android.mylocation.R;
import tugasptm.android.mylocation.model.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Context context;
    List<Location> list;
    itemClickListener listener;

    public LocationAdapter(Context context, List<Location> list, itemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void updateData(List<Location> locations){
        this.list = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Location location = list.get(position);
        holder.tvName.setText(location.getNama());
        holder.tvKet.setText(location.getKota());
        holder.tvLatLng.setText(location.getLat()+" , "+ location.getLng());
        holder.ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(location, false);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemClick(location, true);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvKet, tvLatLng;
        ImageView ivArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvKet = itemView.findViewById(R.id.tv_keterangan);
            tvLatLng = itemView.findViewById(R.id.tv_latlng);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
        }
    }

    public interface itemClickListener{
        void onItemClick(Location location, boolean isLongClick);
    }
}
