package com.example.movielist.data.local

import com.example.movielist.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSource(private val dao: MovieDao) {

    val movie: Flow<List<Movie>> = dao.getMovies().map { it.map { it.toMovie() } }

    suspend fun updateMovie(movie: Movie) {
        dao.updateMovie(movie.toLocalMovie())
    }

    suspend fun insertAll(movies: List<Movie>) {
        dao.insertAll(movies.map { it.toLocalMovie() })
    }

    suspend fun count(): Int {
        return dao.count()
    }
}