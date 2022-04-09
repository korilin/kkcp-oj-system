package com.korilin

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toSecond() = toEpochSecond(ZoneOffset.of("+8"))