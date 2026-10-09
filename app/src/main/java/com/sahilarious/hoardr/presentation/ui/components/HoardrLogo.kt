package com.sahilarious.hoardr.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@Composable
fun HoardrLogo(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Box(
        modifier = modifier
            .width(180.dp)
            .height(90.dp)
            .rotate(-7f),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = "Hoardr",
            fontFamily = Caveat,
            fontSize = 58.sp,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            letterSpacing = (-1.2).sp,
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val path = Path().apply {
                moveTo(
                    x = size.width * 0.255f,
                    y = size.height * 0.82f,
                )

                cubicTo(
                    x1 = size.width * 0.40f,
                    y1 = size.height * 0.79f,
                    x2 = size.width * 0.60f,
                    y2 = size.height * 0.75f,
                    x3 = size.width * 0.72f,
                    y3 = size.height * 0.72f,
                )

                cubicTo(
                    x1 = size.width * 0.76f,
                    y1 = size.height * 0.71f,
                    x2 = size.width * 0.79f,
                    y2 = size.height * 0.69f,
                    x3 = size.width * 0.82f,
                    y3 = size.height * 0.67f,
                )
            }

            drawPath(
                path = path,
                color = colors.primary,
                style = Stroke(
                    width = 3f.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Composable
fun HoardrHeaderLogo(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Box(
        modifier = modifier
            .width(110.dp)
            .height(48.dp)
            .rotate(-4f),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "Hoardr",
            fontFamily = Caveat,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            letterSpacing = (-1.0).sp,
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val path = Path().apply {
                moveTo(
                    x = size.width * 0.05f,
                    y = size.height * 0.82f,
                )
                cubicTo(
                    x1 = size.width * 0.35f,
                    y1 = size.height * 0.79f,
                    x2 = size.width * 0.65f,
                    y2 = size.height * 0.76f,
                    x3 = size.width * 0.85f,
                    y3 = size.height * 0.72f,
                )
            }

            drawPath(
                path = path,
                color = colors.primary,
                style = Stroke(
                    width = 2.8f.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun HoardrLogoPreview() {
    HoardrLogo()
}
