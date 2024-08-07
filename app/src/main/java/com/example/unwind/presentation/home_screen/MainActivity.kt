package com.example.unwind.presentation.home_screen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Places API
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(applicationContext, "")
        }
        placesClient = com.google.android.libraries.places.api.Places.createClient(this)

        setContent {
            PlacesSearchApp(placesClient)
        }
    }
}

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
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                if (newQuery.isNotEmpty()) {
                    searchPlaces(placesClient, newQuery) { results ->
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
                            Toast.makeText(context, "Clicked on ${prediction.getPrimaryText(null)}", Toast.LENGTH_SHORT).show()
                        }
                )
            }
        }
    }
}

private fun searchPlaces(placesClient: PlacesClient, query: String, onResult: (List<AutocompletePrediction>) -> Unit) {
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions
            onResult(predictions)
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
            onResult(emptyList())
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlacesSearchApp() {
    val context = LocalContext.current
    val placesClient = com.google.android.libraries.places.api.Places.createClient(context)
    PlacesSearchApp(placesClient)
}
