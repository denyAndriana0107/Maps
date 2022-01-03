package danu.ga.maps.fragment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    private val _image = MutableLiveData<String>().apply {
        value = "https://drive.google.com/file/d/1DZ4nvcfq_J_oTlHJ9iGXA4zjccLA8_Uk/view"
    }
    val text: LiveData<String> = _text
    val image: LiveData<String> = _image

}