package ru.mattgroy.dota2itemdata.item

import org.springframework.stereotype.Service
import ru.mattgroy.dota2itemdata.scraper.WebScraper

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val webScraper: WebScraper
) {
    fun getAllItemIds(): List<String>? {
        return itemRepository.getAllItemIds()
    }

    fun getItemInfo(itemId: String): Item? {
        return getItemInfoFromLocal(itemId) ?: getItemInfoFromWeb(itemId)
    }

    fun getItemInfoFromLocal(itemId: String): Item? {
        return itemRepository.getByItemId(itemId)
    }

    fun getItemInfoFromWeb(itemId: String): Item? {
        val data = webScraper.retrieveItemData(itemId)
        if (data != null) itemRepository.save(data)
        return data
    }
}
