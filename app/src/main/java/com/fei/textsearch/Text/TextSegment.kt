package com.fei.textsearch.Text

import java.io.Serializable

/**
 * Created by fei on 4/21/2017.
 */

class TextSegment() : Serializable {
    var index: Pair<Int, Int>? = null
    var position: Pair<Int, Int>? = null

    var percent: Double = 0.0
}