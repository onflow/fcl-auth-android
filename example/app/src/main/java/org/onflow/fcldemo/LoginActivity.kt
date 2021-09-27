package org.onflow.fcldemo

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.onflow.fcl.android.auth.AppInfo
import org.onflow.fcl.android.auth.AuthnResponse
import org.onflow.fcl.android.auth.DefaultProvider
import org.onflow.fcl.android.auth.FCL
import java.net.URL

const val EXTRA_ADDRESS = "org.onflow.demo.ADDRESS"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fcl = FCL(
            AppInfo(
                "FCL Demo App",
                URL("https://cryptologos.cc/logos/flow-flow-logo.png"),
            )
        )

        fcl.providers.add(DefaultProvider.DAPPER)
        fcl.providers.add(DefaultProvider.BLOCTO)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_login)

        val dapperLogin = findViewById<Button>(R.id.login_dapper)

        dapperLogin.setOnClickListener {
            fcl.authenticate(this, DefaultProvider.DAPPER) { response ->
                launchProfile(response)
            }
        }

        val bloctoLogin = findViewById<Button>(R.id.login_blocto)

        bloctoLogin.setOnClickListener {
            fcl.authenticate(this, DefaultProvider.BLOCTO) { response ->
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
