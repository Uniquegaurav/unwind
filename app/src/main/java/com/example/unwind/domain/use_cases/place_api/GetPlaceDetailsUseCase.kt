package com.example.unwind.domain.use_cases.place_api

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

object GetPlaceDetailsUseCase {
    private fun fetchPlaceDetails(
        placesClient: PlacesClient,
        predictions: List<AutocompletePrediction>,
        onResult: (List<AutocompletePrediction>) -> Unit
    ) {
        val placeIds = predictions.map { it.placeId }
        val placeFields = listOf(Place.Field.PHOTO_METADATAS)

        val requests = placeIds.map { placeId ->
            placesClient.fetchPlace(
                FetchPlaceRequest.builder(placeId, placeFields).build()
            )
        }

        val combinedRequest = requests.reduce { acc, _ ->
            acc.addOnSuccessListener { response ->
                response.place.photoMetadatas?.let { _ ->
                 //  fetchPhotos(placesClient, photosMetadata)
                }
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
        }

        combinedRequest.addOnCompleteListener {
            // Process and return results with photos
            onResult(predictions)
        }
    }

}