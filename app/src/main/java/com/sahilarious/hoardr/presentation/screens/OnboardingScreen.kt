package com.sahilarious.hoardr.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahilarious.hoardr.presentation.ui.components.HoardrCardStack
import com.sahilarious.hoardr.presentation.ui.components.HoardrLogo
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@Composable
fun HoardrWelcomeScreen(
) {
    val colors = MaterialTheme.hoardrColors

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            HoardrLogo(modifier = Modifier)

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                HoardrCardStack(modifier = Modifier)
            }

            Text(
                text = "Save less. Read more.",
                fontFamily = Caveat,
                fontSize = 22.sp,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your little hoard has places to go.",
                fontFamily = Quicksand,
                fontSize = 13.sp,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .width(230.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = colors.primary
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.textPrimary
                )
            ) {

                Text(
                    text = "Get Started",
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = colors.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnboardingScreenPreview() {
    HoardrTheme {
        HoardrWelcomeScreen()
    }
}
