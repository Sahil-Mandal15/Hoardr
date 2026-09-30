package com.sahilarious.hoardr.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sahilarious.hoardr.R
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@Composable
fun HoardrCardStack(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.hoardrColors
    val isDark = isSystemInDarkTheme()
    val imageRes = if (isDark) {
        R.drawable.card_stack_dark_mode
    } else {
        R.drawable.card_stack_light_mode
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Card Stack Illustration",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HoardrCardStackPreview(modifier: Modifier = Modifier) {
    HoardrTheme {
        HoardrCardStack(modifier)
    }
}