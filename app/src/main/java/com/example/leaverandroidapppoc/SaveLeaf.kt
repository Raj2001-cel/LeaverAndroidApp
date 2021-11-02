package com.example.leaverandroidapppoc

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class SaveLeaf : AppCompatActivity() {
    @Throws(IOException::class)
    fun saveLeaf(context: Context?, Authorization: String, bitmap: Bitmap, isUnhealthy: Boolean, reportId:Long) {
        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Method.POST,
                MyUtil.getProperty("saveleaf.url.path", context)+""+reportId+"/"+isUnhealthy,
                Response.Listener { response ->
                    try {
//                        val obj = JSONObject(String(response.data))
                        Log.d("output", String(response.data))

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
                    params["imageFile"] = DataPart("$imagename.png", getFileDataFromDrawable(bitmap))
                    return params
                }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer "+Authorization
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