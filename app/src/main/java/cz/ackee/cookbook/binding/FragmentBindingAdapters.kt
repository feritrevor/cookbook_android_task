package cz.ackee.cookbook.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import cz.ackee.cookbook.R
import javax.inject.Inject

class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {

    @BindingAdapter("recipeImage")
    fun recipeImage(imageView: ImageView, resId: Int?) {
        if (resId == null) {
            Glide.with(fragment)
                    .load(R.drawable.img_small)
                    .transform(
                            CenterCrop(),
                            RoundedCorners(imageView.context.resources.getDimensionPixelSize(R.dimen.photo_radius))
                    )
                    .into(imageView)
        }
    }
}