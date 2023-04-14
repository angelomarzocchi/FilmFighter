package com.example.filmfighter.network

data class MovieResponse (
    val page: Int,
    val results: List<Movie>
)
