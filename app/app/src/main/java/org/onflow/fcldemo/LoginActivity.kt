package org.onflow.fcldemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.browser.customtabs.CustomTabsIntent
import fcl.Client
import fcl.models.PollingResponse

const val EXTRA_ADDRESS = "org.onflow.demo.ADDRESS"
const val EXTRA_PROFILE = "org.onflow.demo.PROFILE"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.login_dapper)

        login.setOnClickListener {
            val fcl = Client("http://10.0.2.2:3000/api/")
            fcl.requestAuthentication().subscribe({
                auth ->
                if (auth.local == null) {
                    throw Error("not provided login iframe")
                }

                openLoginTab(auth.local?.endpoint!!)

                fcl.getAuthenticationResult(auth, 300).subscribe({
                    Log.e("#AUTH", it.status)

                    // todo refactor to isApproved()
                    if (it.status == "APPROVED") {
                        launchProfile(it)
                    }
                }, {
                    err -> Log.e("#AUTH RESULT ERR", err.localizedMessage)
                })
            }, {
                err -> Log.e("#AUTH SESSION ERR", err.localizedMessage)
            })
        }
    }

    fun openLoginTab(url: String) {
        val tabIntent = CustomTabsIntent.Builder().build()
        tabIntent.launchUrl(this, Uri.parse(url))
    }

    fun launchProfile(auth: PollingResponse) {
        if (auth.data == null) {
            // todo handle edge case
            return
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_ADDRESS, auth.data!!.addr)
            putExtra(EXTRA_PROFILE, auth.data!!.services!!.find { it.type == "authn" }?.endpoint) // todo refactor isAuthn()

        }
        startActivity(intent)
    }
}