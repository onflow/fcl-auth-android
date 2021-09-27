package org.onflow.fcl.android.auth

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import org.onflow.fcl.android.auth.models.ResponseStatus
import java.net.URL

const val DEFAULT_TIMEOUT: Long = 300

data class AppInfo(val title: String, val icon: URL)

enum class ServiceMethod {
    HTTP_POST
}

interface Provider {
    val title: String
    val method: ServiceMethod
    val endpoint: URL
}

data class Providers(
    private val providers: ArrayList<Provider> = ArrayList()
) {
    fun add(provider: Provider) {
        providers.add(provider)
    }

    fun get(provider: Provider): Provider {
        return providers.first { it.endpoint.equals(provider.endpoint) }
    }
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
    )
}

/**
 * Authentication response
 *
 * @property [address] address of the authenticated account
 * @property [status] status of the authentication (approved or declined)
 * @property [reason] if authentication is declined this property will contain more description
 */
data class AuthnResponse(
    val address: String?,
    val status: ResponseStatus,
    val reason: String?
)

/**
 * Flow Client used for interactions with wallet providers
 *
 * @param [appInfo] application info used when interacting with wallets
 */
class FCL(private val appInfo: AppInfo) {

    val providers = Providers()

    /**
     * Starts a new authentication request for the provider.
     * Authentication process includes opening a browser with provided context for the user to sign in
     *
     * @param [context] application context used for opening a browser
     * @param [provider] provider used for authentication
     * @param [onComplete] callback function called on completion with response data
     */
    fun authenticate(
        context: Context,
        provider: Provider,
        onComplete: (AuthnResponse) -> Unit,
    ) {
        val client = Client(providers.get(provider).endpoint.toString())

        client.requestAuthentication().subscribe({ auth ->
            val service = auth.local ?: throw Exception("not provided login iframe")

            this.openLoginTab(context, service.endpoint, service.params)

            client.getAuthenticationResult(auth, DEFAULT_TIMEOUT).subscribe({
                onComplete(AuthnResponse(it.data?.addr, it.status, it.reason))
            }, { err -> throw Exception(err) })
        }, { err -> throw Exception(err) })
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
