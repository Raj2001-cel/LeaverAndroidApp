package com.example.leaverandroidapppoc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream


@Suppress("DEPRECATION")
class Home : AppCompatActivity() {
    lateinit var BSelectImage: Button

    // One Preview Image
    var IVPreviewImage: ImageView? = null

    // constant to compare
    // the activity result code
    var SELECT_PICTURE = 200
    var token:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //fetch the userId and authToken which was stored as sharedpref in sigin
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

         token = prefs.getString("token","")!!


        Log.d("token", "Home : " + token)
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        BSelectImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
                pickImage();
            }

        })
    }


    fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data == null) {
                //Display an error
                return
            }
            if (requestCode === SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data.getData()
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage!!.setImageURI(selectedImageUri)
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
            }
//            val inputStream: InputStream? = data.data?.let { getContentResolver().openInputStream(it) }
//            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
//            System.out.println("inputStream is " + inputStream)


        }
    }
}