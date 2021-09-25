package fcl

import com.google.gson.GsonBuilder
import fcl.models.PollingResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Url

interface RetrofitClient {
    @GET
    fun getAuthentication(@Url url: String): Observable<PollingResponse>

    @POST("authn")
    fun requestAuthentication(): Observable<PollingResponse>

    companion object Factory {
        fun create(url: String): RetrofitClient {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                //.addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit.create(RetrofitClient::class.java)
        }
    }
}