package com.example.leaverandroidapppoc

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class StartReportActivity : AppCompatActivity() {
    var reportId:Long = 0
    var senderName:String = ""
    var date:Long=0
    var token:String=""
    var numberOfUnhealthyLeaves:Long=0
    var totalLeaves:Long=0
    var recieverName:String=""
    var numberOfHealthyLeaves:Long=0
    lateinit var makeReportBtn:FloatingActionButton
    lateinit var txtRecieverName:TextView
    lateinit var editTextReciever:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_report)
        makeReportBtn = findViewById(R.id.makereport)
        txtRecieverName = findViewById(R.id.textrecievername)
        editTextReciever = findViewById(R.id.recievername)


        reportId = intent.getLongExtra("reportId", 0)
        numberOfUnhealthyLeaves = intent.getLongExtra("numberOfUnhealthyLeaves",0)
        totalLeaves = intent.getLongExtra("totalLeaves",0)
        numberOfHealthyLeaves = totalLeaves-numberOfUnhealthyLeaves

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        senderName = sharedPref.getString("email", "dummy").toString()
        token = sharedPref.getString("token", "token").toString()
        val dateDate = Calendar.getInstance().time
        date = dateDate.time

        //logs
        Log.d("intentData","senderName "+senderName+" reportId "+reportId+" totalLeaves "+totalLeaves+" NumberOfUnhealthyLeaves "+numberOfUnhealthyLeaves+" NumberOfHealthyLeaves " +numberOfHealthyLeaves+" Date "+date + " token "+token)

        makeReportBtn.setOnClickListener{
            recieverName = editTextReciever.text.toString().trim()
            Log.d("reciever Name",recieverName)
            //make request to save the report to db
            val saveReport = SaveReport()
            saveReport.saveReport(this,token,reportId,senderName,recieverName,totalLeaves,date,numberOfUnhealthyLeaves,numberOfHealthyLeaves)
        }
    }


}