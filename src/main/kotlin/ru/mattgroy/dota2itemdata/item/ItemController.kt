package ru.mattgroy.dota2itemdata.item

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService
) {
    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: String): ResponseEntity<Item> {
        val data = itemService.getItemInfo(itemId)
        return ResponseEntity(
            data,
            HttpStatus.OK
        )
    }
}
