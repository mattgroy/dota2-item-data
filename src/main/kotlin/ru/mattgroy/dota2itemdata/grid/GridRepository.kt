package ru.mattgroy.dota2itemdata.grid

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GridRepository : JpaRepository<Grid, Long> {
    fun getByType(type: GridType): Grid?
}
