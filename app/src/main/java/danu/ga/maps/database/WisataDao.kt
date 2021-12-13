package unpas.ac.id.maps.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WisataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(wisata: Wisata)

    @Query("SELECT * FROM wisata_master")
    fun readData():LiveData<List<Wisata>>
}