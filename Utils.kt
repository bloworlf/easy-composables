

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    context: Context?,
    permissions: List<String>,
    onPermissionsResult: (Boolean) -> Unit
) {
//    if (permissions.contains("")) {
//        return
//    }
    val permissionAlreadyRequested = rememberSaveable {
        mutableStateOf(false)
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions
    ) {
        permissionAlreadyRequested.value = true
//        if (!it.values.contains(false)) {
//            onPermissionsGranted.invoke()
//        }
        onPermissionsResult(!it.values.contains(false))
    }

    if (!permissionAlreadyRequested.value && !permissionState.shouldShowRationale) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    } else if (permissionState.shouldShowRationale) {
//        ShowRationaleContent {
        permissionState.launchMultiplePermissionRequest()
//        }

    } else {
//        ShowOpenSettingsContent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context?.packageName, null)
        intent.data = uri
        context?.startActivity(intent)
//        }
    }

}