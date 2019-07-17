package app.practice.dogs.view

import android.view.View
import app.practice.dogs.model.DogBreed

interface DogClickListener {
    fun onDogClicked(v: View)
}