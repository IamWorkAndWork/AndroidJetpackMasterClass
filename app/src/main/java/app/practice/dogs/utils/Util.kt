package app.practice.dogs.utils

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import app.practice.dogs.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(Url: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_dog_icon_round)

    Glide.with(context).setDefaultRequestOptions(options)
        .load(Url)
        .into(this)

}

@BindingAdapter("android:ImageUrl")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        view.loadImage(url, getProgressDrawable(view.context))
    }
}