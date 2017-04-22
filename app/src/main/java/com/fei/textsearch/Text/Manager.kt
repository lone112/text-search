package com.fei.textsearch.Text

import android.os.AsyncTask
import java.io.File
import java.util.*

/**
 * Created by fei on 4/21/2017.
 */

class Manager(val root: String) : AsyncTask<Array<String>, Int, List<FileScanResult>>() {
    private var listener: ScanListener? = null

    private var list: ArrayList<FileScanResult> = arrayListOf()

    private var matchFilesCount = 0

    override fun doInBackground(vararg params: Array<String>?): List<FileScanResult> {
        var keywords = params[0]
        start(keywords!!)

        return this.list
    }


    fun start(keywords: Array<String>) {
        this.list.clear()

        var files = listMatchFile()
        matchFilesCount = files.size
        for (path in files) {
            val scanner = TextFileScanner(keywords, false)
            val task = scanner.run(path)
            list.add(task)
            publishProgress(list.size)
        }
    }

    private fun listMatchFile(): List<String> {
        val allTxtFiles = ArrayList<File>()
        listf(this.root, allTxtFiles)
        return allTxtFiles
                .filter { it.extension.toLowerCase().endsWith("txt") }
                .map { it.absolutePath }
                .toList()
    }


    fun getResult(): List<FileScanResult> {
        return this.list
    }

    private fun listf(directoryName: String, files: ArrayList<File>) {
        val directory: File
        if (directoryName.endsWith("/")) {
            directory = File(directoryName)
        } else {
            directory = File(directoryName + "/")
        }

        // get all the files from a directory
        val fList = directory.listFiles() ?: return
        for (file in fList) {
            if (file.isFile) {
                files.add(file)
            } else if (file.isDirectory) {
                listf(file.absolutePath, files)
            }
        }
    }


    fun setScanListener(listener: ScanListener) {
        this.listener = listener
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        var tmp = this.listener
        if (tmp != null) {
            if (this.list.size == 0) {
                tmp.updateStatus("Searching...")
            } else {
                tmp.updateStatus(String.format("Scan files %s / %s", this.list.size, this.matchFilesCount))
            }
        }
    }

    override fun onPostExecute(result: List<FileScanResult>?) {
        super.onPostExecute(result)
        var tmp = this.listener
        if (tmp != null) {
            tmp.updateStatus("Completed " + this.matchFilesCount)
            tmp.completed()
        }
    }
}