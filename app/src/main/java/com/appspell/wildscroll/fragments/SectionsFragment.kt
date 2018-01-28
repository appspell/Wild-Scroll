package com.appspell.wildscroll.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appspell.wildscroll.R
import com.appspell.wildscroll.R.layout
import com.appspell.wildscroll.adapter.SectionsAdapter
import com.appspell.wildscroll.data.DataSource

class SectionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layout.fragment_sections, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SectionsAdapter()
            (adapter as SectionsAdapter).items = DataSource(resources).companies //TODO need to duplicate section items
        }
    }
}