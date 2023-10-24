
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import io.drdroid.polifrontmobile.R

@Composable
fun DialogComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null,
    cancellable: Boolean = true,
    positiveText: String? = null,
    onPositiveClick: () -> Unit = {},
    negativeText: String? = null,
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = cancellable,
            dismissOnClickOutside = cancellable
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier
//                    .background(Color.White)
            ) {

                //.......................................................................
                icon?.let {
                    Image(
                        imageVector = it,
                        contentDescription = null, // decorative
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(top = 35.dp)
                            .height(70.dp)
                            .fillMaxWidth(),
                    )
                }

                content?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 35.dp)
//                            .height(70.dp)
                        ,
                        content = it
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    title?.let {
                        if (it.isNotEmpty()) {
                            TitleTextComponent(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .fillMaxWidth(),
                                textDisplaySize = TextDisplaySize.LARGE,
                                //                            style = MaterialTheme.typography.labelLarge,
                                maxLines = 2,
                                //                            overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    message?.let {
                        if (it.isNotEmpty()) {
                            BodyTextComponent(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                    .fillMaxWidth(),
                                textDisplaySize = TextDisplaySize.MEDIUM
                            )
                        }
                    }
                }
                //.......................................................................

                if (positiveText != null || negativeText != null) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(color = Color.Gray),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        negativeText?.let {
                            TextButton(onClick = {
                                onNegativeClick()
                                onDismiss()
                            }) {
                                if (it.isNotEmpty()) {
                                    TitleTextComponent(
                                        text = it,
                                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                        textDisplaySize = TextDisplaySize.SMALL
                                    )
                                }
                            }
                        }
                        positiveText?.let {
                            if (it.isNotEmpty()) {
                                TextButton(onClick = {
                                    onPositiveClick()
                                    onDismiss()
                                }) {
                                    TitleTextComponent(
                                        text = it,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                        textDisplaySize = TextDisplaySize.MEDIUM
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}