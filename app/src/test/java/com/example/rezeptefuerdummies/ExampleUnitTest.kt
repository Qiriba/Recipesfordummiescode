package com.example.rezeptefuerdummies

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun getItemId(){
        val feedItemList: MutableList<FeedItemModel> = createDummyData()
        val adapter = FeedAdapter(feedItemList)
        assertEquals(adapter.getItemId(1), -693801673)
        assertEquals(adapter.getItemId(0), 1117640830)
    }
    /*
    fun createDummyData(): MutableList<FeedItemModel> {
        val dummyData = mutableListOf<FeedItemModel>()

        // Add your feed items to the list
        dummyData.add(FeedItemModel("https://karlsruhepuls.de/wp-content/uploads/2023/06/Thai-Food-Festival-Karlsruhe.jpg", "Thai Curry", "Hard", "45 Min", "Klassisch", 1))
        dummyData.add(FeedItemModel("https://www.chefsculinar.de/chefsculinar/ds_img/assets_700/2014-09-04-Doener-690x460.jpg", "Döner Kebap", "Medium", "1 H", "Klassisch", 2))
        dummyData.add(FeedItemModel("https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/b36fbe87cb4d6e6e3dce4b23aa35e481/Derivates/563a4efc4ab575cad5db7a9279096132b4334a7c.jpg", "Deutsche Rumpsteak", "Hard", "45 Min", "Klassisch", 3))
        dummyData.add(FeedItemModel("https://www.lidl-kochen.de/images/recipe-wide/860844/veganer-gyrosteller-mit-reis-und-salat-311084.jpg", "Veganer Gyros Teller", "Easy", "30 Min", "Vegan", 4))
        dummyData.add(FeedItemModel("https://img.chefkoch-cdn.de/rezepte/2529831396465550/bilder/1509532/crop-960x720/pfannkuchen-crepe-und-pancake.jpg", "Omas Pfannkuchen", "Easy", "20 Min", "Vegetarisch", 5))
        return dummyData
    }
    */
}
