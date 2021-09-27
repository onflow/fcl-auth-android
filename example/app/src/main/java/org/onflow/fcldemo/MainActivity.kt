package org.onflow.fcldemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import nft.NFTs
import org.onflow.fcldemo.views.NFTListAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addressLabel = findViewById<TextView>(R.id.address)

        val address = intent.getStringExtra(EXTRA_ADDRESS)
        addressLabel.text = address

        displayNFTs(this, address!!)
    }

    private fun displayNFTs(context: Activity, address: String) {
        NFTs.getNFTs(address)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ nfts ->
                Log.e("#NFTs", nfts.nfts.size.toString())
                Log.e("#NFTs", nfts.nfts[0].metadata.title)

                val listView = findViewById<ListView>(R.id.nftList)
                val nftListAdapter = NFTListAdapter(context, nfts.nfts)
                listView.adapter = nftListAdapter
            }, {
                err ->
                Log.e("#NFT ERR", err.toString())
            })
    }
}
