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
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.max

private const val TAG = "Fragment04"

fun limitInRange(v: Int, min: Int, max: Int): Int {
    return kotlin.math.min(max(min, v), max)
}

fun wrapAround(v: Int, min: Int, max: Int): Int {
    if (v < min) return max - (min - v)
    if (v > max) return min + (v - max)
    return v
}

// RecyclerViewへのアイテム追加と削除のサンプルコード。
class Fragment04 : Fragment() {
    companion object {
        const val title = "RecyclerViewアイテム追加・削除サンプル"
    }

    class ViewHolderList(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.titleView)
        val image: ImageView = item.findViewById(R.id.imageView)
    }

    class RecyclerViewAdapter :
        RecyclerView.Adapter<ViewHolderList>() {
        private val list = arrayListOf<String>()

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

        fun insertItem(index: Int, items: Collection<String>) {
            val limitedIndex = limitInRange(index, 0, list.size)
            list.addAll(limitedIndex, items)
            notifyItemRangeInserted(index, items.size)
            // リバインド実行
            // notifyItemRangeChanged(index, items.size)
        }

        fun removeItem(index: Int, size: Int) {
            val limitedIndex = limitInRange(index, 0, list.size)
            val limitedSize = if (limitedIndex + size < list.size) size else list.size - limitedIndex
            repeat(limitedSize) { list.removeAt(limitedIndex) }
            notifyItemRangeRemoved(limitedIndex, limitedSize)
            // リバインド実行
            // notifyItemRangeChanged(index, size)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment04, container, false)
        val activity = activity as MainActivity
        activity.title = title
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerViewAdapter()

        val inputTextIndex = view.findViewById<TextInputEditText>(R.id.inputTextIndex)
        val inputTextSize = view.findViewById<TextInputEditText>(R.id.inputTextSize)

        view.findViewById<Button>(R.id.buttonInsert).setOnClickListener {
            val insertIndex = inputTextIndex.text.toString().toIntOrNull() ?: 0
            val insertSize = inputTextSize.text.toString().toIntOrNull() ?: 0
            Log.d(
                TAG,
                "On click buttion: ${(it as Button).text}, index:$insertIndex, size:$insertSize"
            )

            // 挿入するデータ作成
            val alphabet = ('A'..'Z').toList()
            val items = arrayListOf<String>()
            val offset = recyclerView.adapter!!.itemCount
            repeat(insertSize) { i ->
                val valueIndex = wrapAround(i + offset, 0, alphabet.size)
                items += "${alphabet[valueIndex]}"
            }

            (recyclerView.adapter as RecyclerViewAdapter).insertItem(insertIndex, items)
        }

        view.findViewById<Button>(R.id.buttonRemove).setOnClickListener {
            val index = inputTextIndex.text.toString().toIntOrNull() ?: 0
            val size = inputTextSize.text.toString().toIntOrNull() ?: 0
            Log.d(TAG, "On click buttion: ${(it as Button).text}, index:$index, size:$size")

            (recyclerView.adapter as RecyclerViewAdapter).removeItem(index, size)
        }
        return view
    }
}
