package com.example.ui_samples

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.concurrent.Executors


private const val TAG = "Fragment07"

// 指定されたURLページから画像URLをスクレイピングで取得し、画像をロードするサンプルコード。
class Fragment07 : Fragment() {
    companion object {
        const val title = "指定されたURLページの画像読み込みサンプル"
    }

    class ImageDescription(val uri: String, val title: String)
    class ViewHolderList(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.titleView)
        val image: ImageView = item.findViewById(R.id.imageView)
    }

    class RecyclerViewAdapter() :
        RecyclerView.Adapter<ViewHolderList>() {
        private val list = ArrayList<ImageDescription>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderList {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.fragment03_item, parent, false)
            return ViewHolderList(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
            holder.title.text = list[position].title
            Glide
                .with(holder.itemView.context)
                .load(list[position].uri)
                .into(holder.image)
        }

        override fun getItemCount(): Int = list.size

        fun addItemToHead(item: ImageDescription) {
            list.add(0, item)
            notifyItemRangeInserted(0, 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = activity as MainActivity
        activity.title = Fragment03.title
        val recyclerView = RecyclerView(activity)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerViewAdapter()
        // URL 取得処理
        loadImgSrcUrl { srcUrl, altText ->
            (recyclerView.adapter as RecyclerViewAdapter).addItemToHead(
                ImageDescription(srcUrl, altText)
            )
        }
        return recyclerView
    }

    class MyViewModel : ViewModel() {
        val currentValue = MutableLiveData<String>()
    }

    private var model: MyViewModel? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    private fun loadImgSrcUrl(callback: (String, String) -> Unit) {
        // 参考：https://medium.com/url-memo/viewmodel-viewmodelprovider-c95a19412805
        model = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MyViewModel::class.java)

        executor.execute {
            val document = Jsoup.connect("https://news.livedoor.com/").get()
            val imgList: Elements = document.select("img")
            for (img in imgList) {
                try {
                    Thread.sleep(1000) //3000ミリ秒Sleepする
                } catch (e: InterruptedException) {
                }
                Log.d(TAG, "src:" + img.absUrl("src") + ", alt:" + img.attr("alt"));
                mainThreadHandler.post {
                    callback(
                        img.absUrl("src"),
                        img.attr("alt")
                    )
                }
            }
        }
    }
}