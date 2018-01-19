package com.appspell.wildscroll

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.appspell.wildscroll.data.DataSource


class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val recyclerView: RecyclerView = findViewById(R.id.list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SampleAdapter()
            (adapter as SampleAdapter).items = DataSource(resources).companies
        }
    }
}
