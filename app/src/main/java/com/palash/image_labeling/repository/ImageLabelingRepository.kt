package com.palash.image_labeling.repository

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import javax.inject.Inject

class ImageLabelingRepository @Inject constructor() {

    fun labelImage(bitmap: Bitmap, onSuccess: (List<String>) -> Unit, onFailure: (Exception) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f) // Optional: Adjust the confidence threshold
            .build()
            .let { ImageLabeling.getClient(it) }

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val labelList = labels.map { it.text }
                onSuccess(labelList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
