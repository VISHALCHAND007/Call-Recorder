package com.example.callrecorder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.callrecorder.ui.theme.CallRecorderTheme
import com.example.callrecorder.uicomponents.ShowDialog
import com.example.callrecorder.utils.Constants.Companion.TAG
import com.example.callrecorder.utils.ReadCallLogTextProvider
import com.example.callrecorder.utils.ReadPhoneStateTextProvider
import com.example.callrecorder.utils.RecordAudioPermissionTextProvider
import com.example.callrecorder.viewModel.MainViewModel


class MainActivity : ComponentActivity() {
    private lateinit var phoneNum: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNum = intent.getStringExtra(TAG).toString()
        init()
        setContent {
            CallRecorderTheme {
                val viewModel = viewModel<MainViewModel>()
                val queueDialog = viewModel.requestQueue

                val permissionToRequest = arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.READ_CALL_LOG
                )
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission,
                                perms[permission] == true
                            )
                        }
                    }
                )
                //requesting permission
                LaunchedEffect(key1 = true) {
                    multiplePermissionResultLauncher.launch(permissionToRequest)
                }

                queueDialog
                    .reversed()
                    .forEach { permission ->
                        ShowDialog(
                            permissionTextProvider = when (permission) {
                                android.Manifest.permission.READ_PHONE_STATE -> {
                                    ReadPhoneStateTextProvider()
                                }

                                android.Manifest.permission.RECORD_AUDIO -> {
                                    RecordAudioPermissionTextProvider()
                                }

                                android.Manifest.permission.READ_CALL_LOG -> {
                                    ReadCallLogTextProvider()
                                }

                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = {
                                viewModel.dismissDialog()
                            },
                            onOkClicked = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(permissionToRequest)
                            },
                            onAppSettingClicked = {
                                launchAppSettings()
                                viewModel.dismissDialog()
                            })
                    }
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getListOfFiles()
    }

    private fun init() {
        getListOfFiles()
    }

    fun getListOfFiles() {
        val privateDir = applicationContext.filesDir
        val files = privateDir.listFiles()
    }
}

fun Activity.launchAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

