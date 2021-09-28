package org.onflow.fcldemo.views

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import nft.NFT
import org.onflow.fcldemo.R
import java.net.URL

class NFTListAdapter(private val context: Activity, private val nfts: Array<NFT>) : BaseAdapter() {

    override fun getCount(): Int { return nfts.size }
    override fun getItem(i: Int): Any { return nfts.get(i) }
    override fun getItemId(i: Int): Long { return i.toLong() }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val item = inflater.inflate(R.layout.nft_item, null, true)

        val title = item.findViewById<TextView>(R.id.nftTitle)
        val image = item.findViewById<ImageView>(R.id.nftImage)
        val team = item.findViewById<TextView>(R.id.nftTeam)

        val nft = nfts.get(position)
        Log.e("#EE", nft.toString() + " " + nft.metadata.top_shot_play.toString())

        title.text = nft.metadata.title
        team.text = nft.metadata.top_shot_play.stats.team_at_moment

        loadImage(nft.metadata.image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { img -> image.setImageBitmap(img) },
                { err -> Log.e("#ERR NFT", err.toString()) }
            )

        return item
    }

    private fun loadImage(url: String): Observable<Bitmap> {
        return Observable.create { obs ->
            try {
                val ins = URL(url).openStream()
                obs.onNext(BitmapFactory.decodeStream(ins))
            } catch (e: Exception) {
                obs.onError(e)
            }
        }
    }
}
