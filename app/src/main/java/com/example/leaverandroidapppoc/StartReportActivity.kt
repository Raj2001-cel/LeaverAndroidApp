package com.example.leaverandroidapppoc

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class StartReportActivity : AppCompatActivity() {
    var reportId:Long = 0
    var senderName:String = ""
    var date:Long=0
    var token:String=""

    var result =  arrayOf<JSONObject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_report)
        reportId = intent.getLongExtra("reportId", 0)
        Log.d("reportId", "" + reportId)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        senderName = sharedPref.getString("email", "dummy").toString()
        token = sharedPref.getString("token", "token").toString()
        val dateDate = Calendar.getInstance().time
        date = dateDate.time
        calculateNoOfUnHealthyLeaves()
    }



    fun calculateNoOfUnHealthyLeaves():Long {
        val getAllLeavesByReportId = GetAllLeavesByReportId()


       getAllLeavesByReportId.getAllLeavesByReportId(this, token, reportId)


        return 0
    }


}