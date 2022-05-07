package com.example.ui_samples

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

private const val TAG = "Fragment03"

// MediaStoreに登録されている画像とファイル名の一覧を表示するサンプルコード。
class Fragment03 : Fragment() {
    companion object {
        const val title = "MediaStore読み込みサンプル"
    }
    class ImageDescription(val uri: String, val title: String)
    class ViewHolderList(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.titleView)
        val image: ImageView = item.findViewById(R.id.imageView)
    }
    class RecyclerViewAdapter(private val list: ArrayList<ImageDescription>) :
        RecyclerView.Adapter<ViewHolderList>() {

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
        recyclerView.adapter = RecyclerViewAdapter(readImageDescription())
        return recyclerView
    }

    // MediaStoreに登録されている画像のuriとファイル名の一覧を取得する。
    private fun readImageDescription(): ArrayList<ImageDescription> {
        val result = arrayListOf<ImageDescription>()
        activity!!.applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val colIdxId = cursor.getColumnIndex(
                MediaStore.Images.Media._ID
            )
            val colIdxTitle = cursor.getColumnIndex(
                MediaStore.Images.Media.TITLE
            )

            while (cursor.moveToNext()) {
                val photoUri: Uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getString(colIdxId)
                )
                val photoTitle: String = cursor.getString(colIdxTitle)
                Log.d(TAG, "photoUri $photoUri, title: $photoTitle")

                result += ImageDescription(photoUri.toString(), photoTitle)
            }
        }
        return result
    }
}