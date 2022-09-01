package com.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val screenFlow by viewModel.viewStateFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        )
        {
            items(items = screenFlow.places.keys.toList()) {
                ListItem(text = it, date = screenFlow.places[it]?.first()?.date ?: "")
            }
        }

        screenFlow.errorMessage?.let {
            ErrorMessage(text = it)
        }

        if (screenFlow.loading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Downloading places...")
                LinearProgressIndicator()
            }
        }
    }
}

@Composable
fun ListItem(text: String, date: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(text)
        Text(date)
        Spacer(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}

@Composable
fun ErrorMessage(text: String) {
    Text(modifier = Modifier.background(Color.Cyan), text = text)
}
