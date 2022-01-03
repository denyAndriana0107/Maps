package danu.ga.maps.fragment.ui.jadwalwisata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    private var int:LiveData<Int> ?= null
    fun setFragmentNumber(int: Int):LiveData<Int>{
        this.int = MutableLiveData<Int>().apply {
            value = int
        }
        return this.int as MutableLiveData<Int>
    }


    val i: LiveData<Int>? = this.int
}