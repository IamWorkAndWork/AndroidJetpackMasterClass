package app.practice.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.practice.dogs.R
import app.practice.dogs.databinding.ItemDogBinding
import app.practice.dogs.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {


    fun updateDogList(newDogList: ArrayList<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogViewHolder(v, this)
    }

    override fun getItemCount(): Int {
        return dogsList?.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
//        holder.view.listener = this
        holder.bindItem(dogsList?.get(position))
    }

    override fun onDogClicked(v: View) {
        val uuid = v.dogID.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.uuid = uuid//dogsList?.get(adapterPosition).uuid
        Navigation.findNavController(v).navigate(action)
    }

    inner class DogViewHolder(
        var view: ItemDogBinding,
        dogsListAdapter: DogsListAdapter
    ) : RecyclerView.ViewHolder(view.root) {



        init {

            view.listener = dogsListAdapter

//            itemView?.setOnClickListener {
//                val action = ListFragmentDirections.actionDetailFragment()
//                action.uuid = dogsList?.get(adapterPosition).uuid
//                Navigation.findNavController(it).navigate(action)
//            }

        }

        fun bindItem(dog: DogBreed) {

            view.dog = dog

//            itemView?.name?.text = dog.dogBreed.toString()
//            itemView?.lifeSpan?.text = dog.lifeSpan.toString()
//            view?.imageView?.loadImage(dog?.imageUrl, getProgressDrawable(itemView.context))


        }

    }
}