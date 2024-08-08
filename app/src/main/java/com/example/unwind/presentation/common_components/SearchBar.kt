package com.example.unwind.presentation.common_components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.unwind.domain.use_cases.place_api.GetSearchedPlacesUseCase
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient


@Composable
fun PlacesSearchApp(placesClient: PlacesClient) {
    var query by remember { mutableStateOf("") }
    var placeResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            singleLine = true,
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                if (newQuery.isNotEmpty()) {
                    GetSearchedPlacesUseCase.searchPlaces(placesClient, newQuery) { results ->
                        placeResults = results
                    }
                } else {
                    placeResults = emptyList()
                }
            },
            label = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(placeResults) { prediction ->
                Text(
                    text = prediction.getPrimaryText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "Clicked on ${prediction.getPrimaryText(null)}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                )
            }
        }
    }
}


