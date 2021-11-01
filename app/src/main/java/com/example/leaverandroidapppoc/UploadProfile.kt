package com.example.leaverandroidapppoc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import kotlin.Throws
import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import org.json.JSONException
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class UploadProfile : AppCompatActivity() {
    @Throws(IOException::class)
    fun uploadProfile(context: Context?, Authorization: String, bitmap: Bitmap, textViewOutput: TextView) {
        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Method.POST,
                MyUtil.getProperty("domainNew", context),
                Response.Listener { response ->
                    try {
//                        val obj = JSONObject(String(response.data))
                        Log.d("output", String(response.data))
                        textViewOutput.setText(""+String(response.data).toUpperCase())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
//                    Toast.makeText(applicationContext, "File uploaded", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener { error -> Log.d("error",error.toString()) }) {
            override val byteData: Map<String, DataPart>
                protected get() {
                    val params: MutableMap<String, DataPart> = HashMap()
                    val imagename = System.currentTimeMillis()
                    params["file"] = DataPart("$imagename.png", getFileDataFromDrawable(bitmap))
                    return params
                }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = Authorization
                return headers
            }
        }
        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest)
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}