package app.practice.dogs.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {

//    https://raw.githubusercontent.com/Devtides/DogsApi/master/dogs.json

    @GET("Devtides/DogsApi/master/dogs.json")
    fun getDogs():Single<List<DogBreed>>



}