package com.example.filmfighter.network


import com.example.filmfighter.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val APY_KEY = BuildConfig.API_KEY

private const val BASE_URL = "https://api.themoviedb.org"

private const val SEARCH_URL = "/3/search/movie?api_key=$APY_KEY&language=en-US"

private const val TRENDING_URL= "/3/trending/movie/day?api_key=$APY_KEY"



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TheMovieDBService {
    @GET(TRENDING_URL)
    suspend fun getTrendingMovies(): MovieResponse

    @GET(SEARCH_URL)
    suspend fun getSearchResult(@Query("query") title: String): MovieResponse

}

object TheMovieDB {
    val retrofitService: TheMovieDBService by lazy {
        retrofit.create(TheMovieDBService::class.java)
    }
}

