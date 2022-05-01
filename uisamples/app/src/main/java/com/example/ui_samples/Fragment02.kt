package com.example.ui_samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private val ITEMS = arrayOf(
    "合気道",
    "アイスホッケー",
    "アクアダンス（アクアビクス）",
    "アメリカンフットボール",
    "アーチェリー",
    "アームレスリング",
    "居合道",
    "一輪車",
    "インディアカ",
    "ウィンドサーフィン",
    "ウェーブスキー",
    "ウエイトトレーニング",
    "ウエイトリフティング（重量挙げ）",
)

private val SAMPLE_TEXT = """
    abcefghijklmnopqrstuvwxyz
    あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほ
    0123456789
""".trimIndent()

class ViewHolderList (item: View) : RecyclerView.ViewHolder(item) {
    val title: TextView = item.findViewById(R.id.titleView)
    val image: ImageView = item.findViewById(R.id.imageView)
    val content: TextView = item.findViewById(R.id.contentView)
}

class RecyclerViewAdapter(private val list: Array<String>) : RecyclerView.Adapter<ViewHolderList>() {
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderList {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment02_item, parent, false)
        return ViewHolderList(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
        holder.title.text = list[position]
        holder.content.text = SAMPLE_TEXT
        Glide
            .with(holder.itemView.context)
            .load(android.R.drawable.ic_menu_camera)
            .into(holder.image)
    }

    override fun getItemCount(): Int = list.size
}

class Fragment02 : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = activity as MainActivity
        activity.title = "RecyclerViewサンプル"
        val recyclerView = RecyclerView(activity)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerViewAdapter(ITEMS)
        return recyclerView
    }
}