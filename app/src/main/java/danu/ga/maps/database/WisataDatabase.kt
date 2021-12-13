package unpas.ac.id.maps.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Wisata::class],version = 1,exportSchema = false)
abstract class WisataDatabase:RoomDatabase() {
    abstract fun wisataDao(): WisataDao

    companion object{
        @Volatile
        private var INSTANCE : WisataDatabase?= null

        fun getDatabase(context: Context): WisataDatabase {
            val tempInstance= INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WisataDatabase::class.java,
                        "wisata_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}