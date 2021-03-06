package com.example.leaverandroidapppoc

import android.content.Context
import android.content.Intent
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
import java.io.UnsupportedEncodingException
import java.util.*

class CreateUser : AppCompatActivity() {
    fun userJSON(email: String?, password: String?): JSONObject {
        val `object`: MutableMap<String?, Any?> = HashMap()
        `object`["username"] = email
        `object`["password"] = password
        return JSONObject(`object`)
    }

    fun userHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Accept"] = "application/json; charset=utf-8"
        return headers
    }

    @Throws(IOException::class)
    fun createUser(context: Context, email: String?, password: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
            MyUtil.getProperty("domain", context) + MyUtil.getProperty(
                "create.user.url.path",
                context
            ),
            userJSON(email, password),
            Response.Listener { response: JSONObject? ->
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                if (response != null) {
                    System.out.println("username is "+response.getJSONObject("data").get("username"))
                }
            },

            Response.ErrorListener { error: VolleyError ->
                Log.d("error ", error.toString())
            }) {
            override fun getHeaders(): Map<String, String> {
                return userHeaders()
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
                     Log.d("succes signup ", "headers s" + response.headers)
                    System.out.println("" + response.headers.get("username"))
                    val i = Intent(context, signIn::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(i)
                    Response.success(
                        jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                } catch (e: UnsupportedEncodingException) {
                    Response.error(ParseError(e))
                } catch (je: JSONException) {
                    Response.error(ParseError(je))
                }
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }


}