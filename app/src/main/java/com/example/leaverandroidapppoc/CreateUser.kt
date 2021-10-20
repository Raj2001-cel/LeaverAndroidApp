package com.example.leaverandroidapppoc

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.Response.success
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.HashMap
import kotlin.Result.Companion.success

class CreateUser : AppCompatActivity() {
    fun userJSON( email: String?, password: String?): JSONObject {
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
            MyUtil.getProperty("domain", context) + MyUtil.getProperty("create.user.url.path", context),
            userJSON(email,password),
            Response.Listener { response: JSONObject? ->
                Toast.makeText(context,"Success!", Toast.LENGTH_SHORT).show()
                              },

            Response.ErrorListener { error: VolleyError ->
                Log.d("error ",error.toString())
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
//                    val i = Intent(context, SignInActivity::class.java)
//                    context.startActivity(i)
                     Log.d("succes signup ",response.data)
                    Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    Response.error(ParseError(e))
                } catch (je: JSONException) {
                    Response.error(ParseError(je))
                }
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestQueue.add(jsonObjectRequest)
    }


}