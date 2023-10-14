package com.example.food_app

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class FoodDetail : Fragment() {
    val requestCodeGallery: Int = 1
    val requestPick: Int = 2
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null

    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Resim seçildiğinde yapılması gereken işlemler

            uri?.let {
                try {
                    val imageView = view?.findViewById<ImageView>(R.id.imageView)
                    context?.let { context ->
                        if (android.os.Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            imageView?.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap =
                                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                            imageView?.setImageBitmap(selectedBitmap)
                        }
                        selectedImage = it // Seçilen resmin URI'sini kaydedin
                    }
                } catch (e: Exception) {
                    println("Ex: $e")
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveFood = view.findViewById<Button>(R.id.saveFood)
        val clickImage = view.findViewById<ImageView>(R.id.imageView)

        clickImage.setOnClickListener { selectImage(it) }
        saveFood.setOnClickListener { save(it) }
    }

    private fun save(view: View) {
        println("tıklandı save");
    }

    private fun selectImage(view: View) {
        checkPermissions()
    }

    private fun checkPermissions() {
        activity?.let { act ->
            if (ContextCompat.checkSelfPermission(
                    act,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    act,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    requestCodeGallery
                )
            } else {
                openGallery()
            }
        }
    }


    private fun openGallery() {
        println("openGallery")
        val mimeType = "image/*"
        println("mimeTypes: $mimeType")
        launcher.launch(mimeType)
    }

}