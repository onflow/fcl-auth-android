package org.onflow.fcldemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addressLabel = findViewById<TextView>(R.id.address)

        val address = intent.getStringExtra(EXTRA_ADDRESS)
        addressLabel.text = address
    }
}
