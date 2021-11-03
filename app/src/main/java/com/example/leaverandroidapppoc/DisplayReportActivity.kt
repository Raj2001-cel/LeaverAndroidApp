package com.example.leaverandroidapppoc

import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class DisplayReportActivity : AppCompatActivity() {
    var reportId:Long = 0
    var senderName:String = ""
    var date:Long=0
    var token:String=""
    var numberOfUnhealthyLeaves:Long=0
    var totalLeaves:Long=0
    var recieverName:String=""
    var numberOfHealthyLeaves:Long=0
    lateinit var sharePdfBtn:FloatingActionButton
    lateinit var webView: WebView
    var printJob: PrintJob? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_report)
        reportId = intent.getLongExtra("reportId", 0)
        numberOfUnhealthyLeaves = intent.getLongExtra("numberOfUnhealthyLeaves", 0)
        totalLeaves = intent.getLongExtra("totalLeaves", 0)
        numberOfHealthyLeaves = intent.getLongExtra("noOfHealthyLeaves", 0)
        senderName = intent.getStringExtra("senderName").toString()
        recieverName = intent.getStringExtra("recieverName").toString()
        date = intent.getLongExtra("date", 0)
        token = intent.getStringExtra("token").toString()
        sharePdfBtn = findViewById(R.id.floatingActionButton2)

        val d = Date(date)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var Stringdate  = format.format(d)


        webView = findViewById(R.id.webview)
        val customHtml =
                "<html>" +
                "<body style='font-size:23, align-items:center'>" +
                "<h1>Leaver Report</h1>" +
       " <table border=\"1\">"+

       "<tr>"+
                        "<th>Examination Date</th>"+
                        "<td>"+Stringdate+"</td>"+
       "</tr>"+

        "<tr>"+
                "<th>Examiner Name</th>"+
                "<td>"+senderName+"</td>"+
        "</tr>"+

        "<tr>"+
                        "<th>Authority Center</th>"+
                        "<td>"+recieverName+"</td>"+
        "</tr>"+

        "<tr>"+
               "<th>Healthy Pepper Leaves</th>"+
               "<td>"+numberOfHealthyLeaves+"</td>"+
        " </tr>"+

        "<tr>"+
                "<th>UnHealthy Pepper Leaves</th>"+
                "<td>"+numberOfUnhealthyLeaves+"</td>"+
        " </tr>"+

        "<tr>"+
                "<th>Total Leaves</th>"+
                "<td>"+totalLeaves+"</td>"+
        " </tr>"+
        "</table>"+
                "</body>" +
                "</html>"

        webView.loadData(customHtml, "text/html", "UTF-8")

        sharePdfBtn.setOnClickListener{
            PrintTheWebPage(webView)
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun PrintTheWebPage(webView: WebView) {

        val printManager = this
            .getSystemService(PRINT_SERVICE) as PrintManager

        val jobName = getString(R.string.app_name) + " webpage"


        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        assert(printManager != null)
        printJob = printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }


}


