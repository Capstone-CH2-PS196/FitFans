package com.capstonech2.fitfans.ui.home.menulist

import com.capstonech2.fitfans.R

object MenuDataSource {
    val menu = listOf(
        Menu(
            icon = R.drawable.ic_note,
            name = "Note"
        ),
        Menu(
            icon = R.drawable.ic_collection_border,
            name = "Collection"
        ),
        Menu(
            icon = R.drawable.ic_history,
            name = "History"
        )
    )
}

data class Menu(val icon: Int, val name: String)