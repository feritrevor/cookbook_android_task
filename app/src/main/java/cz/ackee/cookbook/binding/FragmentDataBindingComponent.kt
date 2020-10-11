package cz.ackee.cookbook.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {

    private val fragmentBindingAdapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = fragmentBindingAdapter

}