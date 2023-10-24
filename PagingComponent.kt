
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed class IndicatorType {
    object Circle : IndicatorType()
    object Line : IndicatorType()
}

@Composable
fun LinePagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    animated: Boolean = true,
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val lineWeight = animateFloatAsState(
                targetValue = if (currentPage == iteration) {
                    1.5f
                } else {
                    if (iteration < currentPage) {
                        0.5f
                    } else {
                        1f
                    }
                }, label = "weight", animationSpec = tween(300, easing = EaseInOut)
            )
            val color =
                if (currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
                    .weight(if (animated) lineWeight.value else 1f)
                    .height(4.dp)
            )
        }
    }
}

@Composable
fun CirclePagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    animated: Boolean = true,
) {
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val circleSize = animateDpAsState(
                targetValue = when (currentPage) {
                    iteration -> {
                        16.dp
                    }

                    iteration + 1, iteration - 1 -> {
                        12.dp
                    }

                    else -> {
                        8.dp
                    }
                }, label = "weight", animationSpec = tween(300, easing = EaseInOut)
            )
            val color =
                if (currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(if (animated) circleSize.value else 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPagerComponent(
    displayIndicator: Boolean = true,
    indicatorType: IndicatorType = IndicatorType.Line,
    animated: Boolean = true,
    pages: List<@Composable (() -> Unit)>
) {
    //needs the number of pages
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            pages[page].invoke()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            if (displayIndicator) {
                //indicator
                when (indicatorType) {
                    IndicatorType.Line -> {
                        LinePagerIndicator(
                            pageCount = pages.size,
                            currentPage = pagerState.currentPage,
                            animated = animated
                        )
                    }

                    IndicatorType.Circle -> {
                        CirclePagerIndicator(
                            pageCount = pages.size,
                            currentPage = pagerState.currentPage,
                            animated = animated
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //buttons
            }
        }

    }
}