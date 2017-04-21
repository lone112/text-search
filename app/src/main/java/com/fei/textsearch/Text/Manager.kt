package com.fei.textsearch.Text

import java.io.File
import java.util.*

/**
 * Created by fei on 4/21/2017.
 */

class Manager(val root: String) {
    var totalFileCount: Int = 0
    var list: ArrayList<FileScanResult> = arrayListOf()


    fun start(keywords: Array<String>) {
        for (path in listMatchFile()) {
            val scanner = TextFileScanner(path, keywords)
            val task = scanner.execute(path).get()
            list.add(task)
        }
    }

    fun listMatchFile(): Iterable<String> {
        var matchList = ArrayList<String>()
        val allTxtFiles = ArrayList<File>()
        listf(this.root, allTxtFiles)
        allTxtFiles
                .filter { it.extension.toLowerCase().endsWith("txt") }
                .mapTo(matchList) { it.absolutePath }

        return matchList
    }


    fun getResult(): List<FileScanResult> {
        return this.list
    }

    fun listf(directoryName: String, files: ArrayList<File>) {
        val directory: File
        if (directoryName.endsWith("/")) {
            directory = File(directoryName)
        } else {
            directory = File(directoryName + "/")
        }

        // get all the files from a directory
        val fList = directory.listFiles()
        for (file in fList) {
            if (file.isFile) {
                files.add(file)
            } else if (file.isDirectory) {
                listf(file.absolutePath, files)
            }
        }
    }
}