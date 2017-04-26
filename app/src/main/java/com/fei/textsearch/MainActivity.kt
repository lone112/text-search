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
import com.fei.textsearch.Text.ScanListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val dataCache: ArrayList<FileScanResult> = arrayListOf()

    var manager: Manager = Manager("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var adapter = SimpleAdapter(this, getData(), R.layout.match_result_item,
                arrayOf("ResultCount", "ResultTimes", "ResultFile"),
                intArrayOf(R.id.ResultCount, R.id.ResultTimes, R.id.ResultFile))
        this.listViewResult.adapter = adapter
        this.listViewResult.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
            if (!this.dataCache.isEmpty()) {
                var obj = this.dataCache[position]
                val intent = Intent(this@MainActivity, LinesViewActivity::class.java)
                intent.putExtra("path", obj.file)
                intent.putExtra("words", this.txtSearch.text.toString())
                startActivity(intent)
            }

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
        map.put("ResultFile", "1 Type some key words")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 1)
        map.put("ResultTimes", 1)
        map.put("ResultFile", "2 Select folder")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 11)
        map.put("ResultTimes", 12)
        map.put("ResultFile", "3 Click Search button")
        list.add(map)

        map = HashMap()
        map.put("ResultCount", 5)
        map.put("ResultTimes", 5)
        map.put("ResultFile", "4 Wait ...")
        list.add(map)

        return list
    }

    fun pickFolder(view: View) {
        println("click button")
        val intent = Intent(this@MainActivity, FileChooser::class.java)
        startActivityForResult(intent, 1)
    }

    fun startSearch(view: View) {
        val searchText = this.txtSearch.text.toString().trim()
        if (searchText == "Search Text") {
            this.txtSearch.selectAll()
            return
        }

        if (searchText.isEmpty()) {
            this.txtSearch.selectAll()
            return
        }

        if (this.textViewPath.text.toString() == "Pick folder or file") {
            this.textViewPath.performClick()
            return
        }

        var btn = this.btnSearch
        this.btnSearch.isEnabled = false
        this.btnSearch.text = "..."


        var words = searchText.split(" ").toTypedArray()
        val path = this.textViewPath.text.toString()

        val tmp = Manager(path)
        if (path.startsWith(this.manager.root)) {
            println("use previous file list")
            tmp.searchFilesCache = this.manager.searchFilesCache
        }

        this.manager = tmp

        manager.setScanListener(object : ScanListener {
            override fun updateStatus(status: String) {
                textScanCount.text = status
                println(status)
            }

            override fun completed() {
                println("-------------scan done---------------")
                refreshListView(manager.getResult())
                btn.isEnabled = true
                btn.text = "Search"
            }
        })


        println("search word")
        println(words)
        manager.execute(words)
    }

    private fun refreshListView(list: List<FileScanResult>) {
        dataCache.clear()
        list
                .filter { it.count > 0 }
                .sortedByDescending { it.count }
                .mapTo(dataCache) { it }
        val data = ArrayList<Map<String, Any>>()
        for (scanResult in dataCache) {
            var map: MutableMap<String, Any>
            map = HashMap()
            map.put("ResultCount", scanResult.count)
            map.put("ResultTimes", scanResult.times)
            map.put("ResultFile", scanResult.file.substring(this.manager.root.length + 1, scanResult.file.length))
            data.add(map)
        }

        val adapter = SimpleAdapter(this, data, R.layout.match_result_item,
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
