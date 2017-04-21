package com.fei.textsearch.Text

/**
 * Created by fei on 4/21/2017.
 */

class Manager(val root: String) {
    var totalFileCount: Int = 0
    var list: ArrayList<FileScanResult> = arrayListOf()


    fun start(keywords: Array<String>) {
        for (path in listMatchFile()) {
            val scanner = TextFileScanner(keywords)
            val task = scanner.execute(path)
            list.add(task.get())
        }
    }

    fun listMatchFile(): Iterable<String> {
        throw NotImplementedError()
    }

    fun getResult(): List<FileScanResult> {
        return this.list
    }
}