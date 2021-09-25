package fcl

import fcl.models.PollingResponse
import io.reactivex.Observable
import java.lang.Error
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class Client (url: String) {
    private val api: RetrofitClient = RetrofitClient.create(url)

    fun requestAuthentication(): Observable<PollingResponse> {
        return api.requestAuthentication()
    }

    fun getAuthentication(updateEndpoint: String): Observable<PollingResponse> {
        return api.getAuthentication(updateEndpoint)
    }

    fun authenticateWithResult(secondsTimeout: Long): Observable<PollingResponse> {
        return Observable.create { o ->
            Timer().schedule(secondsTimeout * 1000) {
                o.onError(Error("timeout trying to authenticate"))
            }

            requestAuthentication().subscribe{
                getAuthentication(it.updates!!.endpoint)
                    .repeatWhen { it.delay(500, TimeUnit.MILLISECONDS) }
                    .takeUntil { it.status != "PENDING" }
                    .filter { it.status != "PENDING" }
                    .subscribe{
                        o.onNext(it)
                        o.onComplete()
                    }
            }
        }
    }

}