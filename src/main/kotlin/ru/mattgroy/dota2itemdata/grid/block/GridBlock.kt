package ru.mattgroy.dota2itemdata.grid.block

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import ru.mattgroy.dota2itemdata.grid.Grid
import ru.mattgroy.dota2itemdata.item.Item
import javax.persistence.*

@Entity
class GridBlock(
    val name: String,

    val gridBlockId: Int,

    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY, mappedBy = "gridBlock")
    val items: MutableList<Item> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grid_id")
    var grid: Grid? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0
) {
    fun addItems(items: List<Item?>) {
//        items?.forEach {
//            this.items.add(it)
//            it.gridBlock = this
//        }

//        for (item in items) {
//            this.items.add(item)
//            item.gridBlock = this
//        }

        items.forEach { it?.let {
            this.items.add(it)
            it.gridBlock = this
        }}
    }

    @JsonGetter("gridId")
    fun getGrid(): String? {
        return grid?.type?.ordinal?.toString()
    }

    @JsonGetter("items")
    fun getItemIdsList(): List<String> {
        return items.map { it.itemId }
    }
}
