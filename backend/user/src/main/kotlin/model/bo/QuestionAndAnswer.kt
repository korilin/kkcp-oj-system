package com.korilin.model.bo

import com.korilin.table.Question
import com.korilin.table.UserAnswer

data class QuestionAndAnswer(val question: Question, val answer: UserAnswer?)
