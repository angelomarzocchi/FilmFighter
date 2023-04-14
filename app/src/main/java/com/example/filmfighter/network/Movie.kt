package com.example.filmfighter.network


import com.example.filmfighter.model.Genres
import com.squareup.moshi.Json

private const val POSTER_URL = "https://image.tmdb.org/t/p/w300"

 data class Movie(
    val id: String,
    val adult: Boolean,
    val title: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    val overview: String,
    @Json(name = "genre_ids")
    val genres: List<Int>,
    @Json(name = "vote_average")
    val voteAverage: String
) {
     val absulutePosterPath: String = POSTER_URL + posterPath
     val genresString : List<String> = getGenres(genres)

 }

private fun getGenres(genreList: List<Int>) : List<String>{
    val genreString = mutableListOf<String>()
    genreList.forEach{
        Genres.map[it]?.let { it1 -> genreString.add(it1) }
    }
    return genreString
}