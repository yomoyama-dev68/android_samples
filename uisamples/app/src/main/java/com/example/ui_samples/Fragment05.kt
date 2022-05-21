package com.example.ui_samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.max

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

private const val TAG = "Fragment05"

// RecyclerViewのドラッグ&ドロップのサンプルコード。
class Fragment05 : Fragment() {
    companion object {
        const val title = "RecyclerViewのラッグ&ドロップ"
    }

    class ViewHolderList(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.titleView)
        val image: ImageView = item.findViewById(R.id.imageView)
    }

    class RecyclerViewAdapter(private val list:  Array<String>) :
        RecyclerView.Adapter<ViewHolderList>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderList {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.fragment03_item, parent, false)
            return ViewHolderList(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
            holder.title.text = list[position]
            Glide
                .with(holder.itemView.context)
                .load(android.R.drawable.ic_menu_camera)
                .into(holder.image)
        }

        override fun getItemCount(): Int = list.size

        fun moveItem(from: Int, to: Int) {
            val tmp = list[to]
            list[to] = list[from]
            list[from] = tmp
        }
    }

    private val itemTouchHelper by lazy {
        // 1. drag 方向の引数に上下左右全て指定している。左右も指定した方が自然なドラッグを実現できる。
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as RecyclerViewAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                // 2. モデルの変更。 MainRecyclerViewAdapter でのカスタム実装。
                adapter.moveItem(from, to)
                // 3. Adapter に変更を通知。これを呼ばないと、Drop が完了しない。
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 4. 横方向の swipe 用のコードブロック。ここでは無視。
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activity = activity as MainActivity
        activity.title = title

        val recyclerView = RecyclerView(activity)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerViewAdapter(ITEMS)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return recyclerView
    }
}
