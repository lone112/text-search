package com.fei.textsearch.Text

import android.os.AsyncTask
import java.io.File
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors

/**
 * Created by fei on 4/21/2017.
 */

class Manager(val root: String) : AsyncTask<Array<String>, Int, List<FileScanResult>>() {
    private var listener: ScanListener? = null

    private var resultList: List<FileScanResult> = arrayListOf()

    private var searchFileCount = 0

    var searchFilesCache: List<String> = arrayListOf()

    override fun doInBackground(vararg params: Array<String>?): List<FileScanResult> {
        val keywords = params[0]
        start(keywords!!)

        return this.resultList
    }


    fun start(keywords: Array<String>) {
        if (searchFilesCache.isEmpty()) {
            this.searchFilesCache = listMatchFile()
        }
        searchFileCount = this.searchFilesCache.size

        println("queue size " + searchFileCount)
        val queue = ArrayBlockingQueue<String>(searchFileCount, false, this.searchFilesCache)
        val finishedQueue = ArrayBlockingQueue<FileScanResult>(searchFileCount)

        val executor = Executors.newCachedThreadPool()
        executor.execute(TextFileScannerTask(keywords, false, queue, finishedQueue))
        executor.execute(TextFileScannerTask(keywords, false, queue, finishedQueue))
        executor.execute(TextFileScannerTask(keywords, false, queue, finishedQueue))
        executor.execute(TextFileScannerTask(keywords, false, queue, finishedQueue))

        var previousStatus = 0
        while (queue.size > 0) {

            if (previousStatus != finishedQueue.size) {
                println("update progress")
                previousStatus = finishedQueue.size
                publishProgress(finishedQueue.size)
            }
            Thread.sleep(500)
        }
        println("scan finished")
        resultList = finishedQueue.toList()
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
        return this.resultList
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
            if (values[0] == 0) {
                tmp.updateStatus("Searching...")
            } else {
                tmp.updateStatus(String.format("Scan files %s / %s", values[0], this.searchFileCount))
            }
        }
    }

    override fun onPostExecute(result: List<FileScanResult>?) {
        super.onPostExecute(result)
        var tmp = this.listener
        if (tmp != null) {
            tmp.updateStatus("Completed " + this.searchFileCount)
            tmp.completed()
        }
    }
}