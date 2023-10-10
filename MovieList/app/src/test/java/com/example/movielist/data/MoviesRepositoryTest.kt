package com.example.movielist.data

import com.example.movielist.data.local.LocalDataSource
import com.example.movielist.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class MoviesRepositoryTest {

    @Test
    fun `When Database is empty the server is called`() {
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 0
        }
        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(remoteDataSource) { getMovies() }
    }

    @Test
    fun `When Database is empty the movies are saved into Localstorage`() {
        val expectedMovies = listOf(Movie(1, "Title", "Overview", "poster", false))
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 0
        }
        val remoteDataSource = mock<RemoteDataSource> {
            onBlocking { getMovies() } doReturn expectedMovies
        }
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(localDataSource) { insertAll(expectedMovies) }
    }

    @Test
    fun `When Database is no empty, remote data source is not called`() {
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 1
        }
        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(remoteDataSource, times(0)) { getMovies() }
    }

    @Test
    fun `When Database is not empty, movies are recovered from Database`() {
        val localMovies = listOf(Movie(1, "Title", "Overview", "poster", false))
        val remoteMovies = listOf(Movie(2, "Title2", "Overview2", "poster2", false))
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 1
            onBlocking { movie } doReturn flowOf(localMovies)
        }
        val remoteDataSource = mock<RemoteDataSource> {
            onBlocking { getMovies() } doReturn remoteMovies
        }
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        val result = runBlocking {
            repository.requestMovies()
            repository.movies.first()
        }

        assertEquals(localMovies, result)
    }
}