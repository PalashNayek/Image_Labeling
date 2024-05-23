package com.palash.image_labeling.fragments

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.palash.image_labeling.R
import com.palash.image_labeling.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageLabeler : ImageLabeler
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        /*val imageLabelerOptions = ImageLabelerOptions.Builder().setConfidenceThreshold(0.95f).build()
        imageLabeler = ImageLabeling.getClient(imageLabelerOptions)*/

        //1 Bitmap from drawable
        /*val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.sample_image)*/

        //2 Bitmap from Uri , pick image from camera, gallery, Storage and store the uri of picked image in this variable
        /*val imageUri: Uri? = null
        val bitmap2 = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)*/

        //3 Bitmap from ImageView
        val bitmapDrawable = binding.imageIv.drawable as BitmapDrawable
        val bitmap3 = bitmapDrawable.bitmap

        binding.labelImageBtn.setOnClickListener {
            labelImage(bitmap3)
        }
    }

    private fun labelImage(bitmap: Bitmap) {
        progressDialog.setMessage("Preparing image...")
        progressDialog.show()

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        progressDialog.setMessage("Labeling image")

        imageLabeler.process(inputImage).addOnSuccessListener { imageLabels ->

            for (imageLabel in imageLabels){
                val text = imageLabel.text
                val confidence = imageLabel.confidence
                val index = imageLabel.index

                binding.resultTv.append("text: $text \n confidence: $confidence \n index: $index \n\n")
            }

            progressDialog.dismiss()


        }.addOnFailureListener { e ->
            progressDialog.dismiss()
            Toast.makeText(context, "Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}