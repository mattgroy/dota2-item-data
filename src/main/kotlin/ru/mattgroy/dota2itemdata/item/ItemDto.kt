package ru.mattgroy.dota2itemdata.item

class ItemDto(
    val itemId: String,
    val name: String? = null,
    val icon: ByteArray? = null,
    val price: Int? = null,
    val stats: List<String>? = null,
    val description: String? = null,
    val cooldown: Int? = null,
    val manacost: Int? = null,
//    val notes: String?,
//    val lore: String?,
    val buildsInto: List<String>? = null, // itemId
    val buildsFrom: List<String>? = null // itemId
) {

}