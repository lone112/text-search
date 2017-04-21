package com.fei.textsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import com.fei.textsearch.Chooser.FileChooser
import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import com.fei.textsearch.Text.FileScanResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("kotlin code run...")
        var list = ArrayList<FileScanResult>()
        list.add(FileScanResult(5, 12, "/sdcard/Download/123.txt"))
        list.add(FileScanResult(1, 1, "/sdcard/Download/124.txt"))
        list.add(FileScanResult(12, 12, "/sdcard/Download/125.txt"))
        list.add(FileScanResult(1, 12, "/sdcard/Download/1236.txt"))
        var adapter = SimpleAdapter(this, getData(), R.layout.match_result_item,
                arrayOf("ResultCount", "ResultTimes", "ResultFile"),
                intArrayOf(R.id.ResultCount, R.id.ResultTimes, R.id.ResultFile))
        this.listViewResult.adapter = adapter
    }

    private fun getData(): ArrayList<Map<String, Any>> {
        val list = ArrayList<Map<String, Any>>()

        var map: MutableMap<String, Any> = HashMap()
        map.put("ResultCount", 1)
        map.put("ResultTimes", 12)
        map.put("ResultFile", "/sdcard/Download/1236.txt")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 1)
        map.put("ResultTimes", 1)
        map.put("ResultFile", "/sdcard/Download/1236.txt")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 11)
        map.put("ResultTimes", 12)
        map.put("ResultFile", "/sdcard/Download/1236.txt")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 5)
        map.put("ResultTimes", 5)
        map.put("ResultFile", "/sdcard/Download/1236.txt")
        list.add(map)

        return list
    }

    fun pickFolder(view: View) {
        println("click button")
        val intent = Intent(this@MainActivity, FileChooser::class.java)
        startActivityForResult(intent, 1)
    }

    fun startSearch() {

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
