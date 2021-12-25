package ru.mattgroy.dota2itemdata.item

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.mattgroy.dota2itemdata.grid.block.GridBlock
import javax.persistence.*

@Entity
class Item(
    val itemId: String,
    val name: String?,
    @Lob val icon: ByteArray?,
    val price: Int?,
    @ElementCollection val stats: List<String>?,
    @Lob val description: String?,
    val cooldown: Int?,
    val manacost: Int?,
//    val notes: String? = null,
//    val lore: String? = null,
    @ElementCollection val buildsInto: List<String>?,
    @ElementCollection val buildsFrom: List<String>?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_block_id")
    @JsonIgnore
    var gridBlock: GridBlock? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0
) {
    fun isRecipe(): Boolean {
        return itemId.startsWith("recipe")
    }
}
