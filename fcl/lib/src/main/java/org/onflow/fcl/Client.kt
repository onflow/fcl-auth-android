package org.onflow.fcl

import io.reactivex.rxjava3.core.Observable
import org.onflow.fcl.models.PollingResponse
import java.lang.Error
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

internal class Client(url: String) {
    private val api: RetrofitClient = RetrofitClient.create(url)

    fun requestAuthentication(): Observable<PollingResponse> {
        return api.requestAuthentication()
    }

    fun getAuthentication(updateEndpoint: String): Observable<PollingResponse> {
        return api.getAuthentication(updateEndpoint)
    }

    fun authenticateWithResult(secondsTimeout: Long): Observable<PollingResponse> {
        return Observable.create { o ->
            requestAuthentication().subscribe {
                getAuthenticationResult(it, secondsTimeout)
                    .subscribe(o::onNext, o::onError)
            }
        }
    }

    fun getAuthenticationResult(
        authentication: PollingResponse,
        secondsTimeout: Long
    ): Observable<PollingResponse> {
        return Observable.create { observable ->
            val timeout = Timer().schedule(secondsTimeout * 1000) {
                observable.onError(Error("timeout trying to authenticate"))
            }

            val uri = makeServiceUrl(
                authentication.updates!!.endpoint,
                authentication.updates!!.params,
                "https://foo.com",
            )

            getAuthentication(uri.toString())
                .repeatWhen { it.delay(500, TimeUnit.MILLISECONDS) }
                .takeUntil { it.status != "PENDING" }
                .filter { it.status != "PENDING" }
                .subscribe {
                    observable.onNext(it)
                    observable.onComplete()
                    timeout.cancel()
                }
        }
    }
}
