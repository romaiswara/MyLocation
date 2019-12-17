package fitri.android.mylocation.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocationDao.class}, version = 1, exportSchema = false)
public abstract class LocationDb extends RoomDatabase {

    public abstract InterfaceDao interfaceDao();

    private static volatile LocationDb INSTANCE;

    public static LocationDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationDb.class, "location_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
