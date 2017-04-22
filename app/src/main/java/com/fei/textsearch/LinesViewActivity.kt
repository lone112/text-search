package com.fei.textsearch

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import com.fei.textsearch.Text.TextFileScanner
import com.fei.textsearch.Text.TextSegment
import kotlinx.android.synthetic.main.activity_lines_view.*
import java.io.File


class LinesViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lines_view)
        val path = intent.getStringExtra("path")
        val wordStr = intent.getStringExtra("words")

        println(path)
        println(wordStr)
        var words = wordStr.split(" ").toTypedArray()
        var scanner = TextFileScanner(words, true)
        var result = scanner.run(path)
        for (segment in result.getSegments()) {
            append(segment)
        }
    }

    fun append(segment: TextSegment) {
        val str = SpannableStringBuilder(segment.getSplitText())
        val start = segment.index.first - segment.position.first
        val end = start + segment.index.second - segment.index.first
        str.setSpan(BackgroundColorSpan(Color.YELLOW), start, end, 0)
        str.append("\n")
        str.append("-------[")
        str.append(String.format("%.2f", segment.percent))
        str.append("%]-------")

        val textView = TextView(this)
        textView.text = str
        val layout = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.setMargins(0, 0, 0, 10)
        textView.layoutParams = layout
        textView.setOnClickListener {
            println("click segment")
            val file = File(segment.path)
            var uri = Uri.fromFile(file)
            val mime = "text/*"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, mime)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        this.lines_content.addView(textView)
    }
}
