package app.practice.dogs.model

import androidx.room.*


@Dao
interface DogDao {

    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

//    @Delete
//    suspend fun deleteDog(dog: DogBreed): Long

//    @Update
//    suspend fun updateDog(dog: DogBreed): Long

    @Query("select * from dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("select * from dogbreed where uuid = :dogId")
    suspend fun getDog(dogId: Int): DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()

}