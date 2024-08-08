package com.example.unwind.presentation.home_screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.places.api.net.PlacesClient
import com.example.unwind.presentation.common_components.PlacesSearchApp

class MainActivity : ComponentActivity() {
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(applicationContext, "AIzaSyBUk4Y5DUBK3ms2VH6P6eKAko9e0dvwAng")
        }
        placesClient = com.google.android.libraries.places.api.Places.createClient(this)

        setContent {
            PlacesSearchApp(placesClient)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewPlacesSearchApp() {
    val context = LocalContext.current
    val placesClient = com.google.android.libraries.places.api.Places.createClient(context)
    PlacesSearchApp(placesClient)
}
