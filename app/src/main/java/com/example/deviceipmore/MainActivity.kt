package com.example.deviceipmore

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var myData: HashMap<String,String> =  HashMap<String,String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val status = findViewById(R.id.Status) as TextView
        val ipAddress = findViewById(R.id.IP_Address) as TextView
        val isp = findViewById(R.id.ISP) as TextView
        val country = findViewById(R.id.Country) as TextView
        val region = findViewById(R.id.Region) as TextView
        val organization = findViewById(R.id.Organization) as TextView
        val orgAlsoKnownAs = findViewById(R.id.OrgAlsoKnownAs) as TextView

        val scanButton = findViewById(R.id.ScanButton) as Button

        scanButton.setOnClickListener(){
            val checkIpSite: String = "http://checkip.amazonaws.com"
            val checkInfoAboutIp: String = "http://ip-api.com/json/"
            var myIp: String = ""
            var myRawData: String = ""

            try {
                    myIp = URL(checkIpSite).readText()
                    ipAddress.text = myIp
                    status.text = "SCANNED"
            }catch (e: Exception){
                status.text = "FAILED SCAN"
            }

            if (myIp != ""){
                try {
                    var connect= URL(checkInfoAboutIp+myIp).openConnection()
                    myRawData = (connect.inputStream.bufferedReader().readText()).replace("""[{"}]""".toRegex(),"")

                    myRawData.split(",").forEach{
                        val pair = it.split(":")
                        myData.put(pair[0],pair[1])
                    }
                    //keys: status country countryCode region regionName city zip lat lon timezone isp org as query
                    country.text = myData.get("countryCode") + ":" + myData.get("country")
                    region.text = myData.get("regionName")
                    isp.text = myData.get("isp")
                    organization.text = myData.get("org")
                    orgAlsoKnownAs.text = myData.get("as")



                }catch (e: Exception){
                    println(e.toString())
                    status.text = "FAILED DETAILED SCAN"
                }
            }
            else{
                country.text = ""
                region.text = ""
                isp.text = ""
                organization.text = ""
                orgAlsoKnownAs.text = ""
                ipAddress.text = ""
            }

        }
    }

//    fun gatherInfo(): Map<String, String> {
//        var myIp = ""
//        var myData = ""
//        val Info: HashMap<String, String> = HashMap<String, String>()
//        println("IP")
//
////        var connect: URLConnection = URL("https://checkip.amazonaws.com").openConnection()
////        myIp = connect.inputStream.bufferedReader().readText()
////
////        connect= URL("https://ip-api.com/json/$myIp").openConnection()
////        myData = (connect.inputStream.bufferedReader().readText()).replace("""[{"}]""".toRegex(),"")
//
//        Executors.newSingleThreadExecutor().execute {
//            var connection = URL("https://checkip.amazonaws.com").openConnection()
//            myIp = connection.inputStream.bufferedReader().readText()
//        }
//
//        println("DATA")
//        Executors.newSingleThreadExecutor().execute {
//            val connection = URL("https://ip-api.com/json/$myIp").openConnection()
//            myData = (connection.inputStream.bufferedReader().readText())
//            println(myData)
//        }
//        println(myData)
//
//
//
//        println("PUT")
//        return Info
//    }
}