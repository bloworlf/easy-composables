
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.palette.PalettePlugin
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    context: Context,
    imgUrl: String?,
    updatable: Boolean = false,
    onImageSelected: (Uri) -> Unit = {}
) {
    var imageState by remember { mutableStateOf(imgUrl) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        imageState?.let {
            CoilImage( // GlideImage, FrescoImage
                imageModel = { it },
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
//                        localToggleState = FullScreenState.Active
                    },
                component = rememberImageComponent {
                    // shows a shimmering effect when loading an image.
                    +ShimmerPlugin(
                        baseColor = Color(0xFF424242),
                        highlightColor = Color(0xA3C2C2C2)
                    )
                    +PlaceholderPlugin.Failure(source = Icons.Filled.ImageNotSupported)
                },
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                }
            )
        } ?: run {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(2.dp, Color.Gray, CircleShape)   // add a border (optional)
            )
        }

        if (updatable) {
            SelectImageButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                context = context,
                onImageSelected = {
                    imageState = it.toString()
                    onImageSelected(it)
                }
            )
        }
    }
}

@Composable
fun DisplayImage(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    imgUrl: String?,
    aspectRatio: Float = 16f / 9f,
    updatable: Boolean = false,
    onImageSelected: (Uri) -> Unit = {},
) {
    val context = LocalContext.current

    var imageState by remember { mutableStateOf(imgUrl) }

    Box(
        modifier = modifier
            .clip(shape)
            .border(1.dp, Color.Gray, shape),
        contentAlignment = Alignment.Center
    ) {
        imageState?.let {
            CoilImage( // GlideImage, FrescoImage
                imageModel = { it },
//                imageOptions= ImageOptions(
//                    alignment = Alignment.Center,
//                    contentDescription = "",
//                    contentScale = ContentScale.FillBounds
//                ),
                modifier = Modifier
//                    .size(200.dp)
                    .aspectRatio(aspectRatio)
                    .clip(shape)
                    .clickable {
                        localToggleState = FullScreenState.Active
                    },
                component = rememberImageComponent {
                    // shows a shimmering effect when loading an image.
                    +ShimmerPlugin(
                        baseColor = Color(0xFF424242),
                        highlightColor = Color(0xA3C2C2C2)
                    )
                    +PlaceholderPlugin.Failure(source = Icons.Filled.ImageNotSupported)
                    +PalettePlugin {
//                        palette = it
//                        onPaletteExported.invoke(it)
                    }
                },
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                }
            )
        } ?: run {
            Image(
                imageVector = Icons.Filled.Image,
                contentDescription = "",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
//                    .size(200.dp)
                    .aspectRatio(aspectRatio)
                    .clip(shape),
            )
        }

        if (updatable) {
            SelectImageButton(
                profile = false,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                context = context,
                onImageSelected = {
                    imageState = it.toString()
                    onImageSelected(it)
                }
            )
        }
    }
}