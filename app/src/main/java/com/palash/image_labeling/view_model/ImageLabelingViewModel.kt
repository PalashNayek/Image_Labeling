package com.palash.image_labeling.view_model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palash.image_labeling.repository.ImageLabelingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageLabelingViewModel @Inject constructor(
    private val repository: ImageLabelingRepository
) : ViewModel() {

    private val _labels = MutableLiveData<List<String>>()
    val labels: LiveData<List<String>> get() = _labels

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun labelImage(bitmap: Bitmap) {
        viewModelScope.launch {
            repository.labelImage(bitmap, { labels ->
                _labels.postValue(labels)
            }, { exception ->
                _error.postValue(exception.message)
            })
        }
    }
}
