package com.example.leaverandroidapppoc

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.*

class LoginUtil {
    fun loginJSON(email: String?, password: String?): JSONObject? {
        val `object`: MutableMap<String?, Any?> = HashMap()
        `object`["username"] = email
        `object`["password"] = password
        return JSONObject(`object`)
    }

    fun loginHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Accept"] = "application/json; charset=utf-8"
        return headers
    }

    @Throws(IOException::class)
    fun login(context: Context, email: String?, password: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
                MyUtil.getProperty("domain", context) + MyUtil.getProperty("login.url.path", context),
                loginJSON(email, password),
                Response.Listener { response: JSONObject ->
                    try {
                        if (response != null) {
                            System.out.println("token is "+response.getJSONObject("data").get("token"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error: VolleyError -> ErrorFunction(context, error) }) {
            override fun getHeaders(): Map<String, String> {
                return loginHeaders()
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

                    val i = Intent(context, signIn::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(i)
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
        Log.d("error",error.toString())
    }
}