package com.example.ui_samples

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

private const val WC = ViewGroup.LayoutParams.WRAP_CONTENT
private const val MP = ViewGroup.LayoutParams.MATCH_PARENT
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            addFragment(MenuFragment())
        }
    }

    // Fragmentを表示させるメソッドを定義（表示したいFragmentを引数として渡す）
    private fun addFragment(fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.rootView, fragment)
        transaction.commit()
    }

    // 表示させるFragmentを切り替えるメソッドを定義（表示したいFragmentを引数として渡す）
    fun replaceFragment(fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.rootView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}