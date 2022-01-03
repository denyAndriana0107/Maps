package unpas.ac.id.maps.data

import androidx.lifecycle.LiveData

class Repository(private val wisataDao: WisataDao) {
    val readWisata:LiveData<List<Wisata>> = wisataDao.readData()

    suspend fun insertWisata(wisata: Wisata){
        wisataDao.insertData(wisata)
    }
    suspend fun updateWisata(boolean: Boolean){
        wisataDao.updateData(boolean)
    }
}