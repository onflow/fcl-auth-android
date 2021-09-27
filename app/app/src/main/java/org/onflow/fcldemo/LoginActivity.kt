package org.onflow.fcldemo

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.onflow.fcl.android.AppInfo
import org.onflow.fcl.android.AuthnResponse
import org.onflow.fcl.android.DefaultProvider
import org.onflow.fcl.android.FCL
import java.net.URL

const val EXTRA_ADDRESS = "org.onflow.demo.ADDRESS"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fcl = FCL(
            AppInfo(
                title = "FCL Demo App",
                icon = URL("https://cryptologos.cc/logos/flow-flow-logo.png"),
            )
        )

        fcl.addProvider("dapper", DefaultProvider.DAPPER)
        fcl.addProvider("blocto", DefaultProvider.BLOCTO)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_login)

        val dapperLogin = findViewById<Button>(R.id.login_dapper)

        dapperLogin.setOnClickListener {
            fcl.authenticate(this, "dapper") { response ->
                launchProfile(response)
            }
        }

        val bloctoLogin = findViewById<Button>(R.id.login_blocto)

        bloctoLogin.setOnClickListener {
            fcl.authenticate(this, "blocto") { response ->
                launchProfile(response)
            }
        }
    }

    fun launchProfile(response: AuthnResponse) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_ADDRESS, response.address)
        }

        startActivity(intent)
    }
}
