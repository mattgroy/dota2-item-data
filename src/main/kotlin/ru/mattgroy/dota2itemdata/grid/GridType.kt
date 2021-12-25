package ru.mattgroy.dota2itemdata.grid

enum class GridType(val locName: String) {
    BASICS("Основные"),
    UPGRADES("Улучшения");
//    NEUTRALS("Нейтральные")

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int) = VALUES.firstOrNull { it.ordinal == value }
    }
}
