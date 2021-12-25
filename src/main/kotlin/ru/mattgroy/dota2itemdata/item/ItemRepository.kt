package ru.mattgroy.dota2itemdata.item

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Long> {
    fun getByItemId(itemId: String): Item?

    @Query("SELECT i.itemId FROM Item i")
    fun getAllItemIds(): List<String>
}
