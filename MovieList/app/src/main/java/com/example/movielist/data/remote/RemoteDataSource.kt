package com.example.movielist.data.remote

import com.example.movielist.data.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    suspend fun getMovies(): List<Movie> {
        return Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MovieService::class.java).getMovies().results.map { it.toMovie() }
    }
}