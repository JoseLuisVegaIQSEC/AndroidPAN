package mx.com.iqsec.sdkpan.modules.checkconnection

import android.os.AsyncTask
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CheckInternetTask : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {
        return try {
            val url = URL("https://clients3.google.com/generate_204")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Android")
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 1500
            connection.connect()
            connection.responseCode == 204
        } catch (e: IOException) {
            false
        }
    }
}