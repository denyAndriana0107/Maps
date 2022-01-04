package unpas.ac.id.maps.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unpas.ac.id.maps.data.Wisata
import unpas.ac.id.maps.data.WisataDatabase
import unpas.ac.id.maps.data.Repository
import javax.xml.transform.dom.DOMLocator

class WisataViewModel(application: Application):AndroidViewModel(application) {
    val readData:LiveData<List<Wisata>>
    private val repository: Repository

    init {
        val wisataDao = WisataDatabase.getDatabase(application).wisataDao()
        repository = Repository(wisataDao)
        readData = repository.readWisata
    }

    fun insertData(wisata: Wisata){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertWisata(wisata)
        }
    }
    fun updateData(boolean: Boolean){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateWisata(boolean)
        }
    }
    fun updateCost(cost:Int,jaral:Double){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateCost(cost,jaral)
        }
    }
}