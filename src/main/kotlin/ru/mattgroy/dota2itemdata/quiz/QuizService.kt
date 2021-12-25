package ru.mattgroy.dota2itemdata.quiz

import org.springframework.stereotype.Service
import ru.mattgroy.dota2itemdata.grid.GridService
import ru.mattgroy.dota2itemdata.grid.GridType
import ru.mattgroy.dota2itemdata.item.ItemService
import ru.mattgroy.dota2itemdata.utils.equalsIgnoreOrder

@Service
class QuizService(
    private val gridService: GridService,
    private val itemService: ItemService
) {
    fun createNewQuiz(): Quiz? {
        return gridService
            .getGridBlocks(GridType.UPGRADES)
            ?.random()
            ?.items
            ?.filter { !it.buildsFrom.isNullOrEmpty() }
            ?.random()
            ?.let { item ->
                item.buildsFrom?.let { buildsFrom ->
                    getAllItemsBuildsFrom(buildsFrom.toMutableList(), mutableListOf())
                        .let { allItemsBuildsFrom ->
                            Quiz(
                                item.itemId,
                                allItemsBuildsFrom.count { !it.startsWith("recipe") },
                                allItemsBuildsFrom.count { it.startsWith("recipe") })
                        }
                }
            }
    }

    fun checkQuizSolution(itemId: String, solutionIds: Collection<String>): Boolean {
        return itemService
            .getItemInfo(itemId)
            ?.let { item ->
                item.buildsFrom?.let { getAllItemsBuildsFrom(it.toMutableList(), mutableListOf()).filter { !it.startsWith("recipe") }.equalsIgnoreOrder(solutionIds) }
            } ?: false
    }

    private tailrec fun getAllItemsBuildsFrom(
        currentBuildsFromList: MutableList<String>,
        allBuildsFromList: MutableList<String>
    ): MutableList<String> {
        return if (currentBuildsFromList.isEmpty()) allBuildsFromList
        else {
            currentBuildsFromList.removeLast().let { lastElement ->
                if (itemService.getItemInfo(lastElement)?.buildsFrom?.also { currentBuildsFromList.addAll(it) }
                        .isNullOrEmpty())
                    allBuildsFromList.add(lastElement)
            }

            getAllItemsBuildsFrom(currentBuildsFromList, allBuildsFromList)
        }
    }
}
