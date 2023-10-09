package com.example.movielist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.example.movielist.data.MoviesRepository
import com.example.movielist.data.local.LocalDataSource
import com.example.movielist.data.local.MoviesDatabase
import com.example.movielist.data.remote.RemoteDataSource
import com.example.movielist.ui.screens.home.Home

// 4e44bcbe12d9628a90e27203233519f4

class MainActivity : ComponentActivity() {

  private lateinit var db: MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java, "movies-db"
        ).build()

        val repository = MoviesRepository(
            localDataSource = LocalDataSource(db.movieDao()),
            remoteDataSource = RemoteDataSource()
        )
        setContent {
            Home(repository)
        }
    }
}

