package app.practice.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import app.practice.dogs.model.DogBreed
import app.practice.dogs.model.DogDatabase
import app.practice.dogs.model.DogsApiService
import app.practice.dogs.utils.NotificationHelper
import app.practice.dogs.utils.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferenceHelper(getApplication())
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()
    private var refreshTieme = 5 * 60 * 1000 * 1000 * 1000L//5 minute

    val dogs = MutableLiveData<List<DogBreed>>()

    val dogsLoadError = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    fun refresh() {

        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTieme) {

            fetchFromDatabase()

        } else {
            fetchFromRemote()
        }
//        val dog1 = DogBreed("1", "Corgi", "15 years", "breedGroup", "bredFor", "Temperament", "url")
//        val dog2 = DogBreed("2", "Labrador", "15 years", "breedGroup", "bredFor", "Temperament", "url")
//        val dog3 = DogBreed("3", "Rotwailer", "15 years", "breedGroup", "bredFor", "Temperament", "url")
//
//        val dogList = arrayListOf<DogBreed>(dog1, dog2, dog3)
//
//        dogs?.value = dogList
//        dogsLoadError?.value = false
//        loading?.value = false


    }

    private fun checkCacheDuration() {

        val cachewPreference = prefHelper?.getCacheDuration()
        try {
            val cachePreferenceInt = cachewPreference?.toInt() ?: 5 * 60
            refreshTieme = cachePreferenceInt?.times(1000 * 1000 * 1000L)
        } catch (e: Exception) {

        }

    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsReretrived(dogs)
            Toast.makeText(getApplication(), "Retrived From Database", Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchFromRemote() {

        loading?.value = true

        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(it: List<DogBreed>) {

//                        dogsReretrived(it)

                        storeDogsLocally(it)
                        Toast.makeText(getApplication(), "Retrived From Network", Toast.LENGTH_SHORT).show()

                        NotificationHelper(getApplication()).createNotification()

                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError?.value = true
                        loading?.value = true
                        e.printStackTrace()
                    }
                })
//                .subscribe({
//                    dogs?.value = it
//                    dogsLoadError?.value = false
//                    loading?.value = false
//                }, { er ->
//                    dogsLoadError?.value = true
//                    loading?.value = true
//                    er.printStackTrace()
//                })
        )

    }

    fun refreshByPassCache() {
        fetchFromRemote()
    }

    private fun storeDogsLocally(list: List<DogBreed>) {

        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0;
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsReretrived(list)

        }

        prefHelper?.saveUpdateTime(System.nanoTime())

    }

    private fun dogsReretrived(dogList: List<DogBreed>) {

        dogs?.value = dogList
        dogsLoadError?.value = false
        loading?.value = false

    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}