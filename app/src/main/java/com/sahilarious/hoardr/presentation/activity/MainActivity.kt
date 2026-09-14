package com.sahilarious.hoardr.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sahilarious.hoardr.domain.model.LinkModel
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import com.sahilarious.hoardr.presentation.viewmodel.SampleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HoardrTheme {
                val links by viewModel.linksState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SampleLinkList(
                        links = links,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SampleLinkList(links: List<LinkModel>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(links) { link ->
            Text(
                text = link.url ?: "No URL",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}