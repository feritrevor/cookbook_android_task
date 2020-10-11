package cz.ackee.cookbook.binding

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import cz.ackee.cookbook.R
import cz.ackee.cookbook.databinding.ViewBulletpointTextBinding

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInvisible")
    fun visibleInvisible(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("ingredients")
    fun ingredients(view: LinearLayout, ingredients: ArrayList<String>?) {
        if (ingredients.isNullOrEmpty())
            return
        view.removeAllViews()
        for (ing in ingredients) {
            if (ing.trim().isNotEmpty()) {
                val newView = DataBindingUtil.inflate<ViewBulletpointTextBinding>(
                        LayoutInflater.from(view.context),
                        R.layout.view_bulletpoint_text,
                        view,
                        true
                )
                newView.apply {
                    ingredient = ing
                }
            }
        }

    }

    @JvmStatic
    @BindingAdapter("toolbarImage")
    fun toolbarImage(imageView: ImageView, resId: Int?) {
        if (resId == null) {
            Glide.with(imageView.context)
                    .load(R.drawable.img_big)
                    .into(imageView)
        }
    }
}