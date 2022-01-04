package unpas.ac.id.maps.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WisataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(wisata: Wisata)

    @Query("SELECT * FROM wisata_master")
    fun readData():LiveData<List<Wisata>>

    @Query("UPDATE wisata_master SET button=:boolean WHERE id = 1")
    fun updateData(boolean: Boolean)

    @Query("UPDATE wisata_master SET cost=:cost,jarak=:jarak WHERE id = 1")
    fun updateCost(cost:Int,jarak:Double)

}