package fitri.android.mylocation.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface InterfaceDao {

    @Insert(onConflict = REPLACE)
    void insertLocation(LocationDao locationDaoList);

    // di urut berdasarkan id yg terbesar (data terbaru)
    @Query("SELECT * FROM LocationDao ORDER BY id DESC")
    List<LocationDao> getAllLocation();

    @Query("SELECT * FROM LocationDao WHERE id = :id LIMIT 1")
    LocationDao getLocationById(int id);

    @Query("DELETE FROM LocationDao WHERE id LIKE :id")
    void deleteLocationById(int id);
}
