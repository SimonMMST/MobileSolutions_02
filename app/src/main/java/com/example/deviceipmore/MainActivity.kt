package com.example.deviceipmore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.lang.Exception

const val IP_AND_DETAILS: String = "http://ip-api.com/json/"

class MainActivity : AppCompatActivity() {

    private var myData: HashMap<String, String> = HashMap()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val scanButton = findViewById<Button>(R.id.ScanButton)

        scanButton.setOnClickListener {
            val myRawData: String = getDataFromURL().replace("""[{"}]""".toRegex(), "")

            if (myRawData != "") {
                myRawData.split(",").forEach {
                    val pair = it.split(":")
                    myData[pair[0]] = pair[1]
                }
                setView(true)
            }
            else{
                setView(false)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setView(getData: Boolean) {
        val status : TextView = findViewById(R.id.Status)
        val ipAddress : TextView = findViewById(R.id.IP_Address)
        val isp : TextView = findViewById(R.id.ISP)
        val country : TextView = findViewById(R.id.Country)
        val region : TextView = findViewById(R.id.Region)
        val organization : TextView = findViewById(R.id.Organization)
        val orgAlsoKnownAs : TextView = findViewById(R.id.OrgAlsoKnownAs)

        if (getData) {
            //keys: status country countryCode region regionName city zip lat lon timezone isp org as query
            status.text = myData["status"]?.uppercase()
            country.text = myData["countryCode"] + ":" + myData["country"]
            region.text = myData["regionName"]
            isp.text = myData["isp"]
            organization.text = myData["org"]
            orgAlsoKnownAs.text = myData["as"]
            ipAddress.text = myData["query"]
        } else {
            status.text = "Fail"
            country.text = ""
            region.text = ""
            isp.text = ""
            organization.text = ""
            orgAlsoKnownAs.text = ""
            ipAddress.text = ""
        }
    }

    private fun getDataFromURL(url: String = IP_AND_DETAILS): String {
        return try {
            val connection = URL(url).openConnection()
            connection.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            ""
        }
    }
}