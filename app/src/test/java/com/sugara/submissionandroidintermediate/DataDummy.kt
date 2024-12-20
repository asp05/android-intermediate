package com.sugara.submissionandroidintermediate

import com.sugara.submissionandroidintermediate.data.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val item = ListStoryItem(
                photoUrl = "https://picsum.photos/200/300",
                createdAt = "2021-08-15T00:00:00.000Z",
                name = "Name $i",
                description = "Description $i",
                lon = 0.0,
                id = "id-$i",
                lat = 0.0
            )
            items.add(item)
        }
        return items
    }
}