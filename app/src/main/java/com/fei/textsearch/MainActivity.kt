package com.fei.textsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import com.fei.textsearch.Chooser.FileChooser
import android.app.Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("kotlin code run...")
    }

    fun pickFolder(view: View) {
        println("click button")
        val intent = Intent(this@MainActivity, FileChooser::class.java)
        startActivityForResult(intent, 1)
    }

    fun search(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == (1)) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val strEditText = data.getStringExtra("editTextValue")
                    println(strEditText)
                    this.textViewPath.text = strEditText
                }
            }
        }
    }
}
