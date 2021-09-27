package org.onflow.fcl.android.auth

import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.onflow.fcl.android.auth.models.PollingResponse

class RetrofitClientTest {

    @Test
    @Ignore("manually run")
    fun getAuthenticatedTest() {
        val client = RetrofitClient.create("http://localhost:3000/api/")

        val observer = TestObserver<PollingResponse>()
        client.getAuthentication("http://localhost:3000/api/authn-poll").subscribe(observer)

        observer.await()
        observer.assertComplete()
        observer.assertNoErrors()
        val res = observer.values().first()
        assertEquals("PENDING", res.status)
        assertEquals("HTTP/POST", res.updates?.method)
    }

    @Test
    @Ignore("manually run")
    fun getAuthenticationStatus() {
        val client = RetrofitClient.create("http://localhost:3000/api/")

        val observer = TestObserver<PollingResponse>()
        client.requestAuthentication()
            .subscribe(observer)

        observer.await()
        observer.assertComplete()
        observer.assertNoErrors()
        val res = observer.values().first()
        assertEquals(res.status, "PENDING")
        assertEquals(res.updates?.type, "back-channel-rpc")
        assertEquals(res.updates?.endpoint, "http://localhost:3000/api/authn-poll")
    }
}
