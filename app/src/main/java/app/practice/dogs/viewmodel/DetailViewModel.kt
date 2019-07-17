package app.practice.dogs.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.practice.dogs.model.DogBreed
import app.practice.dogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(dogID: Int) {
//        val dog = DogBreed("1","Corgi","15 Year","breedGroup","BredFor","Temperament","")
//        dogLiveData?.value = dog

        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(dogID)
            dogLiveData?.value = dog
        }
    }

}