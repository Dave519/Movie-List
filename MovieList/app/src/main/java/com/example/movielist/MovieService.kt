package com.example.movielist

import retrofit2.http.GET

interface MovieService {

    @GET("discover/movie?api_key=4e44bcbe12d9628a90e27203233519f4")
    suspend fun getMovies(): MovieResult
}