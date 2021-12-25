package ru.mattgroy.dota2itemdata.item

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService
) {
    @GetMapping
    fun getAllItemIds(): List<String>? {
        return itemService.getAllItemIds()
    }

    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: String): Item? {
        return itemService.getItemInfo(itemId)
    }
}
