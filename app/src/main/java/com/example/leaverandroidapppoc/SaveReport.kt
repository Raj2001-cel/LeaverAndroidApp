package com.example.leaverandroidapppoc

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

class SaveReport:AppCompatActivity() {
    fun reportJSON( reportId:Long , senderName:String , receiverName:String , totalLeaves:Long ,date:Long , noOfUnhealthyLeaves:Long , noOfHealthyLeaves:Long ): JSONObject? {
        val `object`: MutableMap<String?, Any?> = HashMap()
        `object`["reportId"] = reportId
        `object`["senderName"] = senderName
        `object`["receiverName"] = receiverName
        `object`["totalLeaves"] = totalLeaves
        `object`["date"] = date
        `object`["noOfUnhealthyLeaves"] = noOfUnhealthyLeaves
        `object`["noOfHealthyLeaves"] = noOfHealthyLeaves


        return JSONObject(`object`)
    }


    @Throws(IOException::class)
    fun saveReport(context: Context, token:String,reportId:Long , senderName:String , receiverName:String , totalLeaves:Long ,date:Long , noOfUnhealthyLeaves:Long , noOfHealthyLeaves:Long ) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
            MyUtil.getProperty("saveReport.url.path", context),
            reportJSON( reportId , senderName , receiverName, totalLeaves,date , noOfUnhealthyLeaves , noOfHealthyLeaves),
            Response.Listener { response: JSONObject ->
                try {
                    if (response != null) {
                        System.out.println("saveReport Response" + response.getJSONObject("data"))
                        Toast.makeText(context,"Preparing the Report",Toast.LENGTH_LONG).show()
                        val i = Intent(context, DisplayReportActivity::class.java)
                        i.putExtra("token",token)
                        i.putExtra("reportId",reportId)
                        i.putExtra("senderName",senderName)
                        i.putExtra("recieverName",receiverName)
                        i.putExtra("totalLeaves",totalLeaves)
                        i.putExtra("date",date)
                        i.putExtra("noOfUnhealthyLeaves",noOfUnhealthyLeaves)
                        i.putExtra("noOfHealthyLeaves",noOfHealthyLeaves)

                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(i)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError -> ErrorFunction(context, error) }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer "+token
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                return try {
                    val jsonString = String(response.data)
                    val jsonResponse = JSONObject()
                    jsonResponse.put("data", JSONObject(jsonString))
                    jsonResponse.put("headers", JSONObject(response.headers as Map<*, *>))

                    Log.d("headers  ", "headers appearing")
                    Log.d("success_signup_headers ", "headers s" + response.headers)


                    Response.success(
                        jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                    Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response))
                } catch (je: JSONException) {
                    Response.error(ParseError(je))
                }
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestQueue.add(jsonObjectRequest)
    }

    fun ErrorFunction(context: Context?, error: VolleyError) {
        Log.d("error", error.toString())
    }
}