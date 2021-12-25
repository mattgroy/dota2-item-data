package ru.mattgroy.dota2itemdata.grid

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import ru.mattgroy.dota2itemdata.grid.block.GridBlock
import ru.mattgroy.dota2itemdata.item.Item
import javax.persistence.*

@Entity
class Grid(
    @Enumerated(EnumType.ORDINAL)
    @Column(unique = true)
    val type: GridType,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "grid")
    val gridBlocks: MutableList<GridBlock> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0
) {
    fun addBlocks(blocks: List<GridBlock>) {
        for (block in blocks) {
            this.gridBlocks.add(block)
            block.grid = this
        }
    }

    @JsonGetter("type")
    fun getGridType(): String {
        return type.locName
    }
}
