package com.appspell.wildscroll

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appspell.wildscroll.data.Book

class SampleAdapter : RecyclerView.Adapter<ListViewHolder>(), SectionFastScroll {
    var items = emptyList<Book>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder? {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sample_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getSectionName(position: Int): String = items[position].title
}

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author)

    fun bind(book: Book) {
        title.text = book.title
        author.text = book.author
    }
}