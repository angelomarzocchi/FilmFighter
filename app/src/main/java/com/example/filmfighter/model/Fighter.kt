package com.example.filmfighter.model

class Fighter(val name: String,val movie: String) {
    var points: Int = 0
    val answers: Array<Int?> = arrayOfNulls<Int>(10)
}