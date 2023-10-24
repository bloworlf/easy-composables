

@Composable
fun SelectImageButton(
    profile: Boolean = true,
    modifier: Modifier = Modifier,
    context: Context,
    onImageSelected: (Uri) -> Unit
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
//        imageUri = uri
        //do something with imageUri
        uri?.let { onImageSelected(it) }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture(), onResult = {
//            hasImage = it
            if (it) {
                imageUri?.let { uri ->
                    onImageSelected(uri)
                }
            }
        })

    //<editor-fold desc="PERMISSION">
    var displayCameraDialog by remember { mutableStateOf(false) }
    var requestCameraPermission by remember {
        mutableStateOf(false)
    }
    if (requestCameraPermission) {
        RequestPermissions(
            context = context,
            permissions = listOf(Common.PERMISSIONS.PERMISSION_CAMERA)
        ) { granted ->
            if (granted) {
                try {
                    val uri = CustomFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        context.getString(R.string.couldn_t_find_a_camera_application),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCameraPermission = false
        }
    }
    if (displayCameraDialog) {
        DialogComponent(
            cancellable = false,
            title = stringResource(R.string.camera_permission_needed),
            message = stringResource(R.string.camera_permission_message),
            icon = Icons.Filled.PermCameraMic,
            positiveText = stringResource(id = R.string.grant),
            negativeText = stringResource(id = R.string.not_now),
            onPositiveClick = {
                requestCameraPermission = true
            },
            onNegativeClick = {},
            onDismiss = {
                displayCameraDialog = false
            }
        )
    }


    var displayImagePickerDialog by remember { mutableStateOf(false) }
    var requestMediaPermission by remember {
        mutableStateOf(false)
    }
    if (displayImagePickerDialog) {
        DialogComponent(
            cancellable = false,
            title = stringResource(R.string.media_access_permission_needed),
            message = stringResource(R.string.feature_requires_access_to_media),
            icon = Icons.Filled.PermMedia,
            positiveText = stringResource(R.string.grant),
            negativeText = stringResource(R.string.not_now),
            onPositiveClick = {
                requestMediaPermission = true
            },
            onNegativeClick = {},
            onDismiss = {
                displayImagePickerDialog = false
            }
        )
    }
    if (requestMediaPermission) {
        RequestPermissions(
            context = context,
            permissions = Common.PERMISSIONS.PERMISSION_STORAGE.toList()
        ) { granted ->
            if (granted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_not_granted), Toast.LENGTH_SHORT
                ).show()
            }
            requestMediaPermission = false
        }
    }


    var displayDialog by remember { mutableStateOf(false) }
    if (displayDialog) {
        DialogComponent(
            icon = if (profile) {
                Icons.Filled.Person
            } else {
                Icons.Filled.Folder
            },
            title = if (profile) {
                stringResource(R.string.update_profile_picture)
            } else {
                stringResource(id = R.string.update_display_picture)
            },
            message = if (profile) {
                stringResource(R.string.update_profile_picture_from_gallery_or_using_camera)
            } else {
                stringResource(id = R.string.update_display_picture_from_gallery_or_using_camera)
            },
            cancellable = true,
            positiveText = stringResource(R.string.camera),
            negativeText = stringResource(R.string.gallery),
            onPositiveClick = {
                if (!Utils.hasPermissions(
                        context,
                        arrayOf(Common.PERMISSIONS.PERMISSION_CAMERA)
                    )
                ) {
                    //display camera dialog
                    displayCameraDialog = true
                } else {
                    //open camera
                    try {
                        val uri = CustomFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            context,
                            context.getString(R.string.couldn_t_find_a_camera_application),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            onNegativeClick = {
                if (!Utils.hasPermissions(context, Common.PERMISSIONS.PERMISSION_STORAGE)) {
                    //display media access dialog
                    displayImagePickerDialog = true
                } else {
                    //image picker
                    launcher.launch("image/*")
                }
            },
            onDismiss = {
                displayDialog = false
            }
        )
    }
    //</editor-fold>

    Button(
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.customColorsPalette.backgroundColor,
//            disabledContainerColor = MaterialTheme.customColorsPalette.backgroundColor,
//            contentColor = MaterialTheme.customColorsPalette.menuIconColor,
//            disabledContentColor = MaterialTheme.customColorsPalette.menuIconColor,
//        ),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            displayDialog = true
        },
        modifier = modifier
//            .clip(RoundedCornerShape(100.dp))
            .background(color = Color.Transparent)
            .padding(start = 100.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "",
            modifier = Modifier
                .background(color = Color.Transparent)
                .clip(shape = CircleShape)
//                        .size(32.dp)
        )
    }
}