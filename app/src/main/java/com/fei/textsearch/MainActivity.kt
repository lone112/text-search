package com.fei.textsearch

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.fei.textsearch.Chooser.FileChooser
import com.fei.textsearch.Text.FileScanResult
import com.fei.textsearch.Text.Manager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val dataCache: ArrayList<FileScanResult> = arrayListOf()

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
        this.listViewResult.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
            var obj = this.dataCache[position]
            val intent = Intent(this@MainActivity, LinesViewActivity::class.java)
            intent.putExtra("path", obj.file)
            intent.putExtra("words", this.txtSearch.text.toString())
            startActivity(intent)
        }

        val str = SpannableString("Highlighted. Not highlighted.")
        str.setSpan(BackgroundColorSpan(Color.YELLOW), 0, 11, 0)
        this.labelTitle.text = str
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

    fun startSearch(view: View) {
        var manager = Manager("/sdcard/Download") //this.textViewPath.text.toString()
        var words = this.txtSearch.text.split(" ").toTypedArray()
        println("search word")
        println(words)
        manager.start(words)
        refreshListView(manager.getResult())
    }

    private fun refreshListView(list: List<FileScanResult>) {
        dataCache.clear()

        val data = ArrayList<Map<String, Any>>()
        var map: MutableMap<String, Any>
        for (scanResult in list) {
            if (scanResult.count < 1) {
                continue
            }

            dataCache.add(scanResult)

            map = HashMap()
            map.put("ResultCount", scanResult.count)
            map.put("ResultTimes", scanResult.times)
            map.put("ResultFile", scanResult.file)
            data.add(map)
        }

        var adapter = SimpleAdapter(this, data, R.layout.match_result_item,
                arrayOf("ResultCount", "ResultTimes", "ResultFile"),
                intArrayOf(R.id.ResultCount, R.id.ResultTimes, R.id.ResultFile))
        this.listViewResult.adapter = adapter
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
