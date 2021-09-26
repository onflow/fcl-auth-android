package org.onflow.fcldemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import fcl.Client
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = Client("http://10.0.2.2:3000/api/")
        client.authenticateWithResult(20)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                findViewById<TextView>(R.id.helloLabel).text = it.data?.addr
            }, {
                Log.e("ERR", it.localizedMessage)
            })
    }
}