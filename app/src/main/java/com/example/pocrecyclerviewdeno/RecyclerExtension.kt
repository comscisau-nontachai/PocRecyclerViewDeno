package com.example.pocrecyclerviewdeno

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal fun RecyclerView.init() {
    this.setHasFixedSize(true)
    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
    this.layoutManager = layoutManager
}
