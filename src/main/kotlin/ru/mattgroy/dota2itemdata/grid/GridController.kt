package ru.mattgroy.dota2itemdata.grid

import org.springframework.web.bind.annotation.*
import ru.mattgroy.dota2itemdata.grid.block.GridBlock
import ru.mattgroy.dota2itemdata.item.Item

@RestController
@RequestMapping("/grids")
class GridController(
    private val gridService: GridService
) {
    @GetMapping
    fun getGridTypes(): Map<Int, GridType> {
        return gridService.getGridTypes()
    }

    @GetMapping("/all")
    fun getAllGrids(): List<Grid> {
        return gridService.getAllGrids()
    }

    @GetMapping("/{gridType}")
    fun getGridBlocks(@PathVariable gridType: Int): List<GridBlock>? {
        return gridService.getGridBlocks(GridType.getByValue(gridType))
    }

    @GetMapping("/{gridType}/{gridBlockId}")
    fun getGridBlockItems(@PathVariable gridType: Int, gridBlockId: Int): List<Item>? {
        return gridService.getGridBlockItems(GridType.getByValue(gridType), gridBlockId)
    }

    @PutMapping
    fun updateGrids() {
        gridService.updateGrids()
    }
}
