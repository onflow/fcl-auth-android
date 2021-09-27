package org.onflow.fcl.android.auth

import io.reactivex.rxjava3.core.Observable
import org.onflow.fcl.android.auth.models.PollingResponse
import java.lang.Error
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

internal class Client(url: String) {
    private val api: RetrofitClient = RetrofitClient.create(url)

    /**
     * Request new authentication process
     */
    fun requestAuthentication(): Observable<PollingResponse> {
        return api.requestAuthentication()
    }

    /**
     * Get authentication response
     */
    fun getAuthentication(updateEndpoint: String): Observable<PollingResponse> {
        return api.getAuthentication(updateEndpoint)
    }

    /**
     * Get authentication result with finality
     */
    fun getAuthenticationResult(
        authentication: PollingResponse,
        secondsTimeout: Long
    ): Observable<PollingResponse> {
        return Observable.create { observable ->
            val timeout = Timer().schedule(secondsTimeout * 1000) {
                observable.onError(Error("timeout trying to authenticate"))
            }

            if (authentication.updates == null) {
                observable.onError(Throwable("authentication response must include updates"))
            }

            val uri = makeServiceUrl(
                authentication.updates!!.endpoint,
                authentication.updates.params,
                "https://foo.com",
            )

            getAuthentication(uri.toString())
                .repeatWhen { it.delay(1000, TimeUnit.MILLISECONDS) }
                .takeUntil { !it.isPending() }
                .filter { !it.isPending() }
                .subscribe {
                    observable.onNext(it)
                    observable.onComplete()
                    timeout.cancel()
                }
        }
    }
}
