package com.example.movielist.data

import com.example.movielist.data.local.LocalDataSource
import com.example.movielist.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource
) {
    val movies: Flow<List<Movie>> = localDataSource.movie

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }

    suspend fun requestMovies() {
        val isDbEmpty = localDataSource.count() == 0
        if (isDbEmpty) {
            localDataSource.insertAll(remoteDataSource.getMovies())
        }
    }
}