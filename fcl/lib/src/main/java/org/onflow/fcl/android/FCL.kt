package org.onflow.fcl.android

import android.content.Context
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import java.net.URL
import kotlin.collections.HashMap

data class AppInfo(val title: String, val icon: URL)

enum class ServiceMethod {
    HTTP_POST
}

interface Provider {
    val title: String
    val method: ServiceMethod
    val endpoint: URL
}

data class CustomProvider(
    override val title: String,
    override val method: ServiceMethod,
    override val endpoint: URL
) : Provider

enum class DefaultProvider(
    override val title: String,
    override val method: ServiceMethod,
    override val endpoint: URL,
) : Provider {
    DAPPER(
        "Dapper",
        ServiceMethod.HTTP_POST,
        URL("https://dapper-http-post.vercel.app/api/"),
    ),
    BLOCTO(
        "Blocto",
        ServiceMethod.HTTP_POST,
        URL("https://flow-wallet.blocto.app/api/flow/"),
    );
}

data class AuthnResponse(val address: String)

class FCL(private val appInfo: AppInfo) {

    private val providers: HashMap<String, Provider> = HashMap()

    fun addProvider(id: String, provider: Provider) {
        this.providers.put(id, provider)
    }

    fun authenticate(
        context: Context,
        providerId: String,
        onComplete: (AuthnResponse) -> Unit,
    ) {
        // TODO: handle missing provider
        val provider = this.providers.get(providerId)!!

        val client = Client(provider.endpoint.toString())

        client.requestAuthentication().subscribe({
            auth ->
            if (auth.local == null) {
                throw Error("not provided login iframe")
            }

            val service = auth.local

            this.openLoginTab(context, service.endpoint, service.params)

            client.getAuthenticationResult(auth, 300).subscribe({
                Log.e("#AUTH", it.status)

                // todo refactor to isApproved()
                if (it.status == "APPROVED") {
                    onComplete(AuthnResponse(it.data!!.addr))
                }
            }, {
                err ->
                Log.e("#AUTH RESULT ERR", err.localizedMessage)
            })
        }, {
            err ->
            Log.e("#AUTH SESSION ERR", err.localizedMessage)
        })
    }

    private fun openLoginTab(
        context: Context,
        url: String,
        params: Map<String, String>,
    ) {
        val uri = makeServiceUrl(url, params, "http://foo.com")

        val tabIntent = CustomTabsIntent.Builder().build()
        tabIntent.launchUrl(context, uri)
    }
}
