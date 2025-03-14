package com.example.superheroes.data

import retrofit2.http.GET
import retrofit2.http.Path


interface SuperheroService {

    @GET("search/{name}") //llamada para busco el nombre en la API
    suspend fun findSuperheroesByName(@Path("name") query: String): SuperheroResponse //nombre q ponemos al parametro

    @GET("{superhero-id}") //busca el id
    suspend fun findSuperheroesById(@Path("superhero-id") id: String): Superhero

}