package com.korilin.service

import com.korilin.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
internal class QueryService(private val questionRepository: QuestionRepository) {

    internal suspend fun getAllQuestions() = questionRepository.queryAllQuestions()
}