package com.fei.textsearch.Text

import java.util.concurrent.ArrayBlockingQueue

/**
 * Created by fei on 4/26/2017.
 */

class TextFileScannerTask(val keywords: Array<String>, val includeSegment: Boolean, val fileQueue: ArrayBlockingQueue<String>, val finishedQueue: ArrayBlockingQueue<FileScanResult>) : Runnable {
    override fun run() {
        var path = this.fileQueue.poll()
        while (path != null && !path.isEmpty()) {
            val scanner = TextFileScanner(keywords, this.includeSegment)
            val tmp = scanner.run(path)
            this.finishedQueue.add(tmp)
            path = this.fileQueue.poll()
        }
    }
}