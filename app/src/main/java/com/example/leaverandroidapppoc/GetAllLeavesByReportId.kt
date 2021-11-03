package com.example.leaverandroidapppoc

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class GetAllLeavesByReportId:AppCompatActivity() {


    fun getLeavesHeaders(Authorization: String): Map<String, String>? {
        val headers = HashMap<String, String>()
        headers["Authorization"] = Authorization
        headers["Accept"] = "application/json"
        headers["Content-Type"] = "application/json"
        return headers
    }

    @Throws(IOException::class)
    fun getAllLeavesByReportId(context: Context, token: String, reportId: Long){

        var result = ArrayList<JSONObject>()
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                MyUtil.getProperty(
                        "getleaves.url.path",
                        context
                ) + "" + reportId,
                null,
                Response.Listener { response ->
                    Log.d("getresponse", (response).toString())
                    val totalLeaves:Long = response.length().toLong()
                    Log.d("total Leaves", "" + totalLeaves)
                    var numberOfUnhealthyLeaves:Long = 0;
                    for (i in 0 until response.length()) {
                        val objects: JSONObject = response.getJSONObject(i)
                        val key: Iterator<*> = objects.keys()
                        while (key.hasNext()) {
                            val k = key.next().toString()
                            if(objects.getString(k)=="true"){
                                numberOfUnhealthyLeaves++;
                            }
                            println("Key : " + k + ", value : "
                                    + objects.getString(k))
                        }

                        println("-----------")
                    }


                    val i = Intent(context, StartReportActivity::class.java)

                    i.putExtra("reportId", reportId)
                    i.putExtra("totalLeaves",totalLeaves)
                    i.putExtra("numberOfUnhealthyLeaves",numberOfUnhealthyLeaves)
                    context.startActivity(i)

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    error.printStackTrace()
                }) {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        requestQueue.add(jsonObjectRequest)

    }
}


