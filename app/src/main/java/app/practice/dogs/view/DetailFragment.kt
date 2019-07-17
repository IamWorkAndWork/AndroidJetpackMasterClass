package app.practice.dogs.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import app.practice.dogs.R
import app.practice.dogs.databinding.FragmentDetailBinding
import app.practice.dogs.model.DogPalatte
import app.practice.dogs.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class DetailFragment : Fragment() {

    private var uuid: Int? = 0
    var viewModel: DetailViewModel? = null

    private var dataBinding: FragmentDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
//        return inflater.inflate(R.layout.fragment_detail, container, false)
        return dataBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            uuid = DetailFragmentArgs.fromBundle(it).uuid
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java);

        uuid?.let { viewModel?.fetch(it) }

        observeViewModel()

    }

    private fun observeViewModel() {

        viewModel?.dogLiveData?.observe(activity!!, Observer {
            it?.let { dog ->

                dataBinding?.dog = dog

                it?.imageUrl?.let {
                    setupBackgroundColor(it)
                }

//                dogName?.text = dog.dogBreed
//                dogPurpose?.text = dog?.bredFor
//                dogTemperament?.text = dog?.temperament
//                dogLifeSpan?.text = dog?.lifeSpan
//                activity?.let { activity ->
//                    dogImage?.loadImage(dog.imageUrl, getProgressDrawable(activity))
//                }


            }
        })

    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    Palette.from(resource).generate { palette ->
                        val intColor = palette?.lightMutedSwatch?.rgb ?: 0//vibrantSwatch?.rgb ?: 0
                        val myPalette = DogPalatte(intColor)
                        dataBinding?.palatte = myPalette
                    }


                }

            })
    }


}
