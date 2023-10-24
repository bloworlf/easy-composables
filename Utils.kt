
object Utils {
    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun uriToBase64(uri: Uri): String? {
        val contentResolver: ContentResolver = App.instance.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { itStream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (itStream.read(buffer).also { len = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            val imageBytes = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }
        return null
    }

    @Throws(IOException::class)
    fun encodeImage(bm: Bitmap, uri: Uri, context: Context, quality: Int): String? {
        var qual = quality
        var baos = ByteArrayOutputStream()
        val rotatedBitmap: Bitmap? =
            rotateBitmap(bm, uri, context)
        rotatedBitmap?.let {
            while ((it.allocationByteCount > (300 * 1024)) && (qual > 50)) {
                qual -= 2
                baos = ByteArrayOutputStream()
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, qual, baos)
            }
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
        return null
    }

    @Throws(IOException::class)
    fun rotateBitmap(bm: Bitmap, uri: Uri, context: Context): Bitmap? {
        val ei =
            ExifInterface(getPath(uri, context))
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        var rotatedBitmap: Bitmap? = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
                bm, 90
            )

            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
                bm, 180
            )

            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
                bm, 270
            )

            ExifInterface.ORIENTATION_NORMAL -> bm
            else -> bm
        }
        return rotatedBitmap
    }

    fun getPath(uri: Uri?, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(projection[0])
        val filePath = cursor.getString(columnIndex)
        val path = cursor.getString(column_index)
        cursor.close()
        //        yourSelectedImage = BitmapFactory.decodeFile(filePath);
        return path
    }

    @SuppressLint("DiscouragedApi")
    fun Context.findStringResourceByName(name: String): String {
        val packageName: String = packageName
        val resId: Int = resources
            .getIdentifier(name, "string", packageName)
        return if (resId == 0) {
            name
        } else {
            getString(resId)
        }
    }
}

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