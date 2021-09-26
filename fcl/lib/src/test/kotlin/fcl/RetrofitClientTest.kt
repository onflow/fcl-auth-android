package fcl

import fcl.models.PollingResponse
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import kotlin.test.assertEquals

class RetrofitClientTest {

    @Test
    fun getAuthenticatedTest() {
        val client = RetrofitClient.create("http://localhost:3000/api/")

        val observer = TestObserver<PollingResponse>()
        client.getAuthentication("http://localhost:3000/api/authn-poll").subscribe(observer)

        observer.await()
        observer.assertComplete()
        observer.assertNoErrors()
        val res = observer.values().first()
        assertEquals(res.status, "PENDING")
        assertEquals(res.updates?.method, "HTTP/POST")
        assertEquals(res.local?.method, "VIEW/IFRAME")
    }

    @Test
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

    /*
    @Test
    fun authenticateWithResult() {
        val client = Client("http://localhost:3000/api/")

        val observer = TestObserver<PollingResponse>()

        client.authenticateWithResult(10)
            .subscribe(observer)

        observer.assertNoErrors()
        observer.await()
        observer.assertComplete()
        val auth = observer.values().first()
        assertEquals(auth.data?.addr, "0x7fc8cf73ba231d10")
    }
     */
}