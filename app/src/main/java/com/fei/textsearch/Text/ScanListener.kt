package com.fei.textsearch.Text

/**
 * Created by lone112 on 4/22/2017.
 */

interface ScanListener {
    fun updateStatus(status: String)
    fun completed()
}