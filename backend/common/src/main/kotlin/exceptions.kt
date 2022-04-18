package com.korilin

private const val RELEASE_STATUS_EXIST = "There has a contest with the status"
private const val CONTEST_NOT_FOUND = "Could not found Contest"
private const val CONTEST_STATUS_NOT_FOUND = "Could not found Contest status"

class AbnormalStatusException(message: String = RELEASE_STATUS_EXIST) :
    RuntimeException(message)

class ContestNotFoundException(message: String = CONTEST_NOT_FOUND) : RuntimeException(message) {
    constructor(id: Int) : this("$CONTEST_NOT_FOUND by id=$id")
}

class ContestStatusNotFoundException(message: String = CONTEST_STATUS_NOT_FOUND) : RuntimeException(message) {
    constructor(id: Int) : this("$CONTEST_STATUS_NOT_FOUND by id=$id")
}

class CompileFailureException(override val message: String): RuntimeException()