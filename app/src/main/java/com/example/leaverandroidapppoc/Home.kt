package com.example.leaverandroidapppoc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException


@Suppress("DEPRECATION")
class Home : AppCompatActivity() {
    lateinit var BSelectImage: Button
    lateinit var galleryBtn: Button
    // One Preview Image
    var IVPreviewImage: ImageView? = null

    // constant to compare
    // the activity result code
    var SELECT_PICTURE_CAMERA = 0
//    var SELECT_PICTURE = 200
    var token:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //fetch the userId and authToken which was stored as sharedpref in sigin
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

         token = prefs.getString("token", "")!!


        Log.d("token", "Home : " + token)
        BSelectImage = findViewById(R.id.BSelectImage);
        galleryBtn  = findViewById(R.id.capture)
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        BSelectImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
//                pickImage();
//                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, SELECT_PICTURE)
                if(checkAndRequestPermissions()) {
                   takePictureFromGallery()
                }
            }

        })

        galleryBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
//                pickImage();
//                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, SELECT_PICTURE)
                if(checkAndRequestPermissions()) {
                    takePictureFromCamera()
                }
            }

        })

    }

    private fun takePictureFromGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 1)
    }

    private fun takePictureFromCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(packageManager) != null) {
            startActivityForResult(takePicture, 2)
        }
    }


   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                val selectedImageUri = data?.data
                this.IVPreviewImage?.setImageURI(selectedImageUri)
                val bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImageUri
                )
                Log.d("bitmap",bitmap.toString())
                try {
                    val uploadProfile = UploadProfile()
                    uploadProfile.uploadProfile(

                            applicationContext,

                            token,
                            bitmap
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            2 -> if (resultCode == RESULT_OK) {
                val bundle = data?.extras
                val bitmapImage = bundle!!["data"] as Bitmap?
                IVPreviewImage?.setImageBitmap(bitmapImage)
                try {
                    val uploadProfile = UploadProfile()
                    if (bitmapImage != null) {
                        uploadProfile.uploadProfile(

                                applicationContext,

                                token,
                                bitmapImage
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val cameraPermission = ActivityCompat.checkSelfPermission(this@Home, Manifest.permission.CAMERA)
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this@Home, arrayOf(Manifest.permission.CAMERA), 2)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera()
        } else Toast.makeText(this@Home, "Permission not Granted", Toast.LENGTH_SHORT).show()
    }
    fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_PICTURE_CAMERA)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
//            if (data == null) {
//                //Display an error
//                return
//            }
//            if (requestCode === SELECT_PICTURE) {
//                // Get the url of the image from data
//                val selectedImageUri: Uri? = data.getData()
//                val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//            IVPreviewImage!!.setImageBitmap(bitmap)
////                if (null != selectedImageUri) {
////                    // update the preview image in the layout
////                    IVPreviewImage!!.setImageURI(selectedImageUri)
////                    val bitmap = MediaStore.Images.Media.getBitmap(
////                            this.contentResolver,
////                            selectedImageUri
////                    )
////Log.d("bitmap", bitmap.toString())
////
////                    try {
////                        val uploadProfile = UploadProfile()
////                        uploadProfile.uploadProfile(
////
////                                applicationContext,
////
////                                token,
////                                bitmap
////                        )
////                    } catch (e: IOException) {
////                        e.printStackTrace()
////                    }
////                }
//            }
////            val inputStream: InputStream? = data.data?.let { getContentResolver().openInputStream(it) }
////            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
////            System.out.println("inputStream is " + inputStream)
//
//
//        }
//    }
}