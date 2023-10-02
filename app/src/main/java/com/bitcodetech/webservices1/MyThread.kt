package com.bitcodetech.webservices1

import android.content.Context
import android.util.Log

class MyThread(
    val context : Context
) : Thread() {

    override fun run() {
        //Util.simpleHttpReqRes("https://reqres.in/api/users?page=2")
        val users = Util.getUsers()
            if(users != null) {
            for(user in users) {
                Log.e("tag", user.toString())
                Util.downloadImage(
                    context,
                    user.avatar,
                    user.firstName + "-" + user.lastName + ".jpg"
                )
            }
        }

    }
}