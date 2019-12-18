package tugasptm.android.mylocation.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface InterfaceDao {

    @Insert(onConflict = REPLACE)
    void insertLocation(Location locationList);

    // di urut berdasarkan id yg terbesar (data terbaru)
    @Query("SELECT * FROM Location ORDER BY id DESC")
    List<Location> getAllLocation();

    @Query("SELECT * FROM Location WHERE id = :id LIMIT 1")
    Location getLocationById(int id);

    @Query("DELETE FROM Location WHERE id LIKE :id")
    void deleteLocationById(int id);
}
