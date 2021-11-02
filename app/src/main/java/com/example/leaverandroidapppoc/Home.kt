package com.example.leaverandroidapppoc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException


@Suppress("DEPRECATION")
class Home : AppCompatActivity() {

    lateinit var  startReport: ImageButton
    lateinit var BSelectImage: Button
    lateinit var galleryBtn: Button
    lateinit var textViewOutput: TextView
    lateinit var floatingActionButtonSave: FloatingActionButton
    // One Preview Image
    val reportId:Long?=getRandomReportId()
    var IVPreviewImage: ImageView? = null
    var Output:String?=null
    // constant to compare
    // the activity result code
    var bitmap:Bitmap?=null
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
        startReport =  findViewById(R.id.imageButton)
        BSelectImage = findViewById(R.id.BSelectImage);
        galleryBtn  = findViewById(R.id.capture)
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        textViewOutput = findViewById(R.id.textView)
        floatingActionButtonSave = findViewById(R.id.saveleaf)
        BSelectImage.setOnClickListener { // Do some work here

            if (checkAndRequestPermissions()) {
                takePictureFromGallery()
            }
        }

        galleryBtn.setOnClickListener {
            if (checkAndRequestPermissions()) {
                takePictureFromCamera()
            }
        }

        floatingActionButtonSave.setOnClickListener { // Do some work here
           val saveLeaf = SaveLeaf()
            var output = textViewOutput.text.toString()
            val isUnHealthty = isUnHealthy(output)
            bitmap?.let { it1 ->
                reportId?.let { it2 ->
                    saveLeaf.saveLeaf(

                            applicationContext,
                            token,
                            it1,
                            isUnHealthty,
                            it2

                            )
                }
            }
        }

        startReport.setOnClickListener{
            val i = Intent(this, StartReportActivity::class.java)
            Log.d("rid", "inside home report id is "+reportId)
            i.putExtra("reportId",reportId)
            startActivity(i)
        }

    }

    private fun isUnHealthy(output: String): Boolean {
        if (output.equals("unhealthy")){
            return true;
        }
        return false
    }

    private fun getRandomReportId(): Long{
        val number = Math.floor(Math.random() * 9000000000L).toLong() + 1000000000L
        return number
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
                val bitmapnew = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImageUri
                )
                bitmap  = bitmapnew
                Log.d("bitmap", bitmap.toString())
                try {
                    val uploadProfile = UploadProfile()
                    uploadProfile.uploadProfile(

                            applicationContext,

                            token,
                            bitmapnew ,
                            textViewOutput
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
                                bitmapImage, textViewOutput
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