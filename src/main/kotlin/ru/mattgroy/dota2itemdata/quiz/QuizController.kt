package ru.mattgroy.dota2itemdata.quiz

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/quiz")
class QuizController(
    private val quizService: QuizService
) {
    @GetMapping
    fun getNewItemForQuiz(): Quiz? {
        return quizService.createNewQuiz()
    }

    @PostMapping("/{itemId}")
    fun checkItemBuildsFrom(@PathVariable itemId: String, @RequestBody solutionIds: Collection<String>): Boolean {
        return quizService.checkQuizSolution(itemId, solutionIds)
    }
}
