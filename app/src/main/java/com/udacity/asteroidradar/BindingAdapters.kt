package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy





/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("pictureOfDay")
fun ImageView.setPictureOfDay(pictureOfDay: PictureOfDay?) {
    if (pictureOfDay?.mediaType == "image")
        Glide.with(context).load(pictureOfDay.url)
            .error(R.drawable.ic_status_potentially_hazardous)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    else setImageResource(R.drawable.ic_status_potentially_hazardous)

    contentDescription = if (pictureOfDay?.title.isNullOrEmpty())
        context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    else context.getString(
        R.string.nasa_picture_of_day_content_description_format,
        pictureOfDay?.title
    )

}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
      if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
