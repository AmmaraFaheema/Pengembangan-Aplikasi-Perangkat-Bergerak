package com.example.bab5_navigationcompose

object Routes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val ABOUT = "about"
    const val DETAIL = "detail/{studentId}"
    fun detail(studentId: Int): String {
        return "detail/$studentId"
    }
}