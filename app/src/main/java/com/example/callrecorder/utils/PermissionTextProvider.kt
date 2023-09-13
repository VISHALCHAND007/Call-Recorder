package com.example.callrecorder.utils

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}