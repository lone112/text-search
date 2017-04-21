package com.fei.textsearch

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fei.textsearch.Text.TextSegment
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.fei.textsearch.Text.FileScanResult
import kotlinx.android.synthetic.main.activity_lines_view.*
import java.io.File


class LinesViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lines_view)

        val result = intent.getSerializableExtra("lines") as FileScanResult
        for (segment: TextSegment in result.list()) {
            append(segment)
        }

    }

    fun append(segment: TextSegment) {
        val str = SpannableString(segment.getSplitText())
        str.setSpan(BackgroundColorSpan(Color.YELLOW), segment.index.first, segment.index.second, 0)
        val textView = TextView(this)
        textView.text = str
        textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.setOnClickListener {
            println("click segment")
            val file = File(segment.path)
            val intent = Intent(Intent.ACTION_VIEW, Uri.fromFile(file))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(intent)
        }
        this.lines_content.addView(textView)
    }
}
