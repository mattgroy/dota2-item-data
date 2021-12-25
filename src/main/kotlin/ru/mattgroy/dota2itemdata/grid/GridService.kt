package ru.mattgroy.dota2itemdata.grid

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import ru.mattgroy.dota2itemdata.grid.block.GridBlock
import ru.mattgroy.dota2itemdata.item.Item
import ru.mattgroy.dota2itemdata.scraper.WebScraper
import ru.mattgroy.dota2itemdata.utils.pmap
import javax.transaction.Transactional


private val logger = KotlinLogging.logger {}

@Service
class GridService(
    private val resourceLoader: ResourceLoader,
    private val gridRepository: GridRepository,
    private val webScraper: WebScraper
) {
    private val mapper = jacksonObjectMapper()

    fun getGridTypes(): Map<Int, GridType> {
        return GridType.values().mapIndexed { i, gridType -> i to gridType }.toMap()
    }

    fun getAllGrids(): List<Grid> {
        return gridRepository.findAll()
    }

    fun getGridBlocks(gridType: GridType?): List<GridBlock>? {
        return gridType?.let { gridRepository.getByType(gridType)?.gridBlocks }
    }

    fun getGridBlockItems(gridType: GridType?, gridBlockId: Int): List<Item>? {
        return gridType?.let { gridRepository.getByType(gridType)?.gridBlocks?.first { block -> block.gridBlockId == gridBlockId }?.items }
    }

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun updateGrids() = runBlocking(Dispatchers.Default) {
        val itemGridFile = resourceLoader.getResource("classpath:itemGrid.json")
        val itemGridMap = mapper.readValue<Map<Int, Map<Int, ItemGridBlockJson>>>(itemGridFile.file)

        for ((gridType, gridBlock) in itemGridMap) {
            GridType.getByValue(gridType)?.let { type ->
                gridBlock.pmap { (gridBlockId, gridBlockInfo) ->
                    GridBlock(gridBlockInfo.name, gridBlockId).also { block ->
                        block.addItems(gridBlockInfo.items.pmap {
                            webScraper.retrieveItemData(it)?.also {
                                logger.info { "retrieved info on '${it.itemId}'" }
                            }
                        })
                    }
                }.let { gridBlocks -> gridRepository.save(Grid(type).also { it.addBlocks(gridBlocks) }) }
            }
        }
        gridRepository.flush()
        logger.info { "changes flushed to db, 'updateGrids' is completed" }
    }
}
