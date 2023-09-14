package com.example.callrecorder.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.coroutineContext

class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    val requestQueue = mutableStateListOf<String>()
    private val filesLiveData: MutableLiveData<List<File>> = MutableLiveData()

    fun dismissDialog() {
        requestQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !requestQueue.contains(permission))
            requestQueue.add(permission)
    }

    fun getFiles() {
        val fileLocation = application.filesDir
        val files = fileLocation.listFiles()
        filesLiveData.value = files as List<File>
    }
}