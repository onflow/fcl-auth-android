package nft

import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Owner(val owner: String, val nfts: Array<NFT>)
data class NFT(val id: String, val metadata: NFTMetadata)
data class NFTMetadata(val title: String, val image: String, val topShotPlay: NFTPlay)
data class NFTPlay(val stats: NFTStats)
data class NFTStats(val teamAtMoment: String, val playerName: String, val jerseyNumber: String)

internal interface RetrofitClient {
    @GET("nfts")
    fun getNFTs(@Query("owner") address: String): Observable<Owner>

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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit.create(RetrofitClient::class.java)
        }
    }
}


object NFTs {
    private val api = RetrofitClient.create("https://flow-nft-api-mock.vercel.app/api/v1/")

    fun getNFTs(address: String): Observable<Owner> {
        return api.getNFTs(address)
    }
}