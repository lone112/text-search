package com.fei.textsearch.Text

import android.os.AsyncTask
import java.io.File
import java.io.FileInputStream

/**
 * Created by fei on 4/21/2017.
 */

class TextFileScanner(val keywords: Array<String>) : AsyncTask<String, Int, FileScanResult>() {
    override fun doInBackground(vararg p0: String?): FileScanResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun run(file: String): FileScanResult {

        var list = ArrayList<TextSegment>()

        var map = HashMap<String, Int>()

        val allText = readWholeFile(file)

        var ind: Int = 0

        for (kw in keywords) {
            ind = allText.indexOf(kw)
            while (ind != -1) {
                if (map.containsKey(kw)) {
                    map[kw] = map[kw]!! + 1
                } else {
                    map.put(kw, 1)
                }
                val segment = TextSegment(file, ind, kw, allText)
                list.add(segment)
                ind = allText.indexOf(kw, ind + 1)
            }
        }

        val result = FileScanResult(map.size, map.values.sum(), file)
        result.addSegments(list)
        return result
    }

    fun readWholeFile(path: String): String {
        try {
            val file = File(path)

            val fis = FileInputStream(file)
            val data = ByteArray(file.length().toInt())
            fis.read(data)
            fis.close()
            val str = String(data, charset("GB2312"))
            return str
        } catch (e: Exception) {
            return ""
        }
    }
}