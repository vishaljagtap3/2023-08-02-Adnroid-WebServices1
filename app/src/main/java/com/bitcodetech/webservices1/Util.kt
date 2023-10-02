package com.bitcodetech.webservices1

import android.content.Context
import android.os.Environment
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object Util {

    fun simpleHttpReqRes(url: String) {

        val url = URL(url)
        val httpUrlConnection = url.openConnection() as HttpURLConnection

        httpUrlConnection.requestMethod = "GET"
        //httpUrlConnection.doOutput = true
        httpUrlConnection.doInput = true
        //httpUrlConnection.outputStream.write("somedata".toByteArray())

        httpUrlConnection.connect()


        Log.e("tag", "req method: ${httpUrlConnection.requestMethod}")
        Log.e("tag", "res code: ${httpUrlConnection.responseCode}")
        Log.e("tag", "res message: ${httpUrlConnection.responseMessage}")
        Log.e("tag", "res type: ${httpUrlConnection.contentType}")
        Log.e("tag", "res len: ${httpUrlConnection.contentLength} ${httpUrlConnection.contentEncoding}")

        val inStream = httpUrlConnection.inputStream
        val data = ByteArray(1024)
        var count = 0

        count = inStream.read(data)
        while (count != -1) {
            Log.e("tag", String(data, 0, count))
            count = inStream.read(data)
        }

        inStream.close()
        httpUrlConnection.disconnect()

    }

    fun getUsers() : ArrayList<User>? {
        val httpURLConnection = URL("https://reqres.in/api/users?page=2").openConnection() as HttpURLConnection
        httpURLConnection.connect()

        if(httpURLConnection.responseCode != 200) {
            return null
        }

        val responseBuffer = StringBuffer()
        var count = 0
        val data = ByteArray(1024)

        count = httpURLConnection.inputStream.read(data)
        while(count != -1) {
            responseBuffer.append(String(data, 0, count))
            count = httpURLConnection.inputStream.read(data)
        }

        Log.e("tag", responseBuffer.toString())

        val jRes = JSONObject(responseBuffer.toString())
        jRes.getInt("page")

        val jUsers = jRes.getJSONArray("data")

        val users = ArrayList<User>()
        for(i in 0..jUsers.length() - 1) {
            val jUser = jUsers.getJSONObject(i)

            users.add(
                User(
                    jUser.getInt("id"),
                    jUser.getString("email"),
                    jUser.getString("first_name"),
                    jUser.getString("last_name"),
                    jUser.getString("avatar")
                )
            )
        }

        return users

    }

    fun downloadImage(context : Context, url : String, fileName : String) {
        val httpUrlCon = URL(url).openConnection() as HttpURLConnection
        httpUrlCon.connect()

        if(httpUrlCon.responseCode != 200) {
            return
        }

        val appRootDir = context.getExternalFilesDir(null)
        val userImagesDir = File(appRootDir, "user_images")
        //val userImagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if(!userImagesDir.exists()) {
            userImagesDir.mkdir()
        }

        val imageFile = File(userImagesDir, fileName)
        val out = FileOutputStream(imageFile)

        var count = 0;
        val data = ByteArray(1024 * 2)
        count = httpUrlCon.inputStream.read(data)
        while( count != -1) {
            out.write(data, 0, count)
            count = httpUrlCon.inputStream.read(data)
        }
        out.close()
    }

}