package com.example.callrecorder.utils

class RecordAudioPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems like you permanently declined this permission, click on the button below and allow the Record Audio permission their."
        } else {
            "This permission is required to access the microphone for recording your calls."
        }
    }
}

class ReadPhoneStateTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems like you permanently declined this permission, click on the button below and allow the Read Phone State permission their."
        } else {
            "This permission is required to read the phone state. Required to notify the app when a call is received."
        }
    }
}
class ReadCallLogTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems like you permanently declined this permission, click on the button below and allow the Read Call Record permission their."
        } else {
            "This permission is required to read the call record."
        }
    }
}
