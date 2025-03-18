package co.feip.fefu2025

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.net.NetworkCapabilities
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.util.Log

class MainActivity : AppCompatActivity() {

    private var counter = 0
    private lateinit var connectivityManager: ConnectivityManager

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                val internetConnected = isInternetConnected()
                Log.d("InternetStatus", "Интернет подключён: $internetConnected")
            }
        }
    }

    private fun isInternetConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(MyBroadcastReceiver(), filter)

        counter = savedInstanceState?.getInt("counter", 0) ?:0
        val clickView: TextView = findViewById(R.id.clickView)
        clickView.text= "Количество кликов: $counter"
        clickView.setOnClickListener {
            counter++
            clickView.text= "Количество кликов: $counter"
        }
    }

    override fun onSaveInstanceState(saveData: Bundle) {
        super.onSaveInstanceState(saveData)
        saveData.putInt("counter", counter)
    }

}