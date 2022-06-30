package com.course.pedometer

interface StepListener {
    fun step(timeNs: Long)
}