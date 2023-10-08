package com.example.food_app

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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class FoodDetail : Fragment() {
    val requestCodeGallery: Int = 1
    val requestConnect: Int = 2
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let { act ->
                ActivityCompat.requestPermissions(
                    act,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    requestCodeGallery
                )

            }
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity?.let {
                ActivityCompat.startActivityForResult(
                    it,
                    intent,
                    requestConnect,
                    null
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCodeGallery) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activity?.let {
                    ActivityCompat.startActivityForResult(
                        it,
                        intent,
                        requestConnect,
                        null
                    )
                }
            } else {
                Toast.makeText(context, "İzin reddedildi.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("onActivityResult")
        if (requestCode == requestConnect && data != null) {
            try {
                selectedImage = data.data
                val imageView = view?.findViewById<ImageView>(R.id.imageView)
                context?.let { it1 ->
                    if (selectedImage != null) {
                        if (android.os.Build.VERSION.SDK_INT >= 28) {
                            val fileSource = ImageDecoder.createSource(
                                it1.contentResolver,
                                selectedImage!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(fileSource)
                            println("selectedBitmap: $selectedBitmap")
                            imageView!!.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                it1.contentResolver,
                                selectedImage
                            )
                            imageView!!.setImageBitmap(selectedBitmap)
                        }
                    }
                }

                return
            } catch (e: Exception) {
                println("Ex: $e")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


//        val requestPermission =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//                if (isGranted) {
//                    checkPermissions()
//                } else {
//                    Toast.makeText(
//                        context,
//                        "İzin reddedildi.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
}