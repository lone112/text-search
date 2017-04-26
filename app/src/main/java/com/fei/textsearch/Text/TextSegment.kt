package com.fei.textsearch.Text

import java.io.Serializable

/**
 * Created by fei on 4/21/2017.
 */

class TextSegment(val path: String, start: Int, val keyworkd: String, private val allText: String) : Serializable {
    var index: Pair<Int, Int> = Pair(0, 0)
    var percent: Double = 0.0
    var position: Pair<Int, Int> = Pair(0, 0)

    init {
        index = Pair(start, start + keyworkd.length)
        percent = start * 100.0 / allText.length

        val step = 40

        var pStart: Int = start - step
        if (pStart < 0) {
            pStart = 0
        }

        var pEnd = start + keyworkd.length + step
        if (pEnd > allText.length) {
            pEnd = allText.length
        }

        position = Pair(pStart, pEnd)
    }

    fun getSplitText(): String {
        return allText.substring(position.first, position.second)
    }

}