package com.example.rezeptefuerdummies

import android.content.Context
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class FeedAdapterTest {

    private lateinit var adapter: FeedAdapter
    private lateinit var feedItems: MutableList<FeedItemModel>
    private lateinit var parent: ViewGroup
    @Before
    fun setUp() {
        // Prepare the feed item list
        feedItems = mutableListOf(
            FeedItemModel("Image_Url_1", "Chicken Soup", "Easy", "30 mins", "Soup", 1,"example"),
            FeedItemModel("Image_Url_2", "Beef Stew", "Hard", "45 mins", "Main Course", 2,"example")
        )
        adapter = FeedAdapter(feedItems)

        val context = ApplicationProvider.getApplicationContext<Context>()
        parent = mock(ViewGroup::class.java)
        `when`(parent.context).thenReturn(context)
    }

    @Test
    fun `getItemCount returns correct size`() {
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `getItemId returns stable unique ID based on position`() {
        val id1 = adapter.getItemId(0)
        val id2 = adapter.getItemId(1)
        assertEquals(feedItems[0].hashCode().toLong(), id1)
        assertEquals(feedItems[1].hashCode().toLong(), id2)
    }


    @Test
    fun `addItems adds items and notifies adapter`() {
        val newItems = listOf(
            FeedItemModel("image_url_3", "Veggie Pasta", "Medium", "25 mins", "Pasta", 3,"")
        )
        adapter.addItems(newItems)

        assertEquals(3, adapter.itemCount)
    }
}