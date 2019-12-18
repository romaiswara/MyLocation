package tugasptm.android.mylocation.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    int id;
    String nama, kota;
    double lat, lng;

    public Location(String nama, String kota, double lat, double lng) {
        this.nama = nama;
        this.kota = kota;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
