package com.example.unwind.domain.use_cases.place_api

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient

object SearchPhotosUseCase {
    private fun fetchPhotos(
        placesClient: PlacesClient,
        photosMetadata: List<PhotoMetadata>
    ) {
        photosMetadata.forEach { photoMetadata ->
            placesClient.fetchPhoto(FetchPhotoRequest.builder(photoMetadata).build())
                .addOnSuccessListener { photoResponse ->
                    val photoBitmap = photoResponse.bitmap
                    // Handle the photo (e.g., display it or store it)
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
        }
    }

}