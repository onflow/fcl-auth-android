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

        val addressLabel = findViewById<TextView>(R.id.address)

        val address = intent.getStringExtra(EXTRA_ADDRESS)
        addressLabel.text = address
    }
}