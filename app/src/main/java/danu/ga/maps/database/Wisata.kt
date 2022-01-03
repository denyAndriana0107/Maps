package unpas.ac.id.maps.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Wisata_master")
data class Wisata (
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        val button:Boolean,
        val cost:Int,
        val jarak:Double
        )
