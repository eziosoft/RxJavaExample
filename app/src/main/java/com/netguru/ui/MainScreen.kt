package com.netguru.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.netguru.rxjavaexample2.SharedViewModel

@Composable
fun MainScreen(viewModel: SharedViewModel) {
    val screenFlow by viewModel.viewStateFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            Search(
                onSearch = {
                    viewModel.search(it)
                })

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            )
            {
                items(items = screenFlow.places.keys.toList()) {
                    ListItem(
                        text = it,
                        startDate = screenFlow.places[it]?.first()?.startDate ?: "",
                        endDate = screenFlow.places[it]?.first()?.endDate ?: ""
                    )
                }
            }

            screenFlow.errorMessage?.let {
                ErrorMessage(text = it)
            }
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
fun Search(onSearch: (String) -> Unit) {
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(10.dp)
    ) {
        Text(text = "Search")
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(0.9f),
                value = text,
                maxLines = 1,
                singleLine = true,
                onValueChange = {
                    text = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                keyboardActions = KeyboardActions(
                    onDone = { onSearch(text) })
            )

            IconButton(modifier = Modifier
                .size(24.dp)
                .weight(0.1f),
                onClick = { onSearch(text) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "contentDescription",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ListItem(text: String, startDate: String, endDate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(text)
        Text("$startDate - $endDate")
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
