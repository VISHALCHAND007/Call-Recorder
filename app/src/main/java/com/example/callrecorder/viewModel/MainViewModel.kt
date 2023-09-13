package com.example.callrecorder.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val requestQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        requestQueue.removeFirst()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !requestQueue.contains(permission))
            requestQueue.add(permission)
    }
}