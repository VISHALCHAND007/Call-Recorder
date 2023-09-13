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
import com.example.callrecorder.utils.ReadPhoneStateTextProvider
import com.example.callrecorder.utils.RecordAudioPermissionTextProvider
import com.example.callrecorder.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallRecorderTheme {
                val viewModel = viewModel<MainViewModel>()
                val queueDialog = viewModel.requestQueue

                val permissionToRequest = arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.READ_PHONE_STATE
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
            }
        }
    }
}
fun Activity.launchAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}