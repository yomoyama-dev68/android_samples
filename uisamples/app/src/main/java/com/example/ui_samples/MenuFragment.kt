package com.example.ui_samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

// メニューリスト
private val MENU_DATA = mapOf(
    "ConstraintLayoutサンプル" to { Fragment01() })

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 所属している親アクティビティを取得
        val activity = activity as MainActivity
        activity.title = "メニュー"

        // メニューリスト設定
        val listView = ListView(activity)
        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_list_item_1,
            MENU_DATA.keys.toList()
        )
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, _, position, _ ->
            val key = adapterView.getItemAtPosition(position) as String
            Toast.makeText(
                activity,
                "$key",
                Toast.LENGTH_SHORT
            ).show()
            activity.replaceFragment(MENU_DATA[key]!!.invoke())
        }
        return listView
    }
}