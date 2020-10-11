package cz.ackee.cookbook.screens.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import cz.ackee.cookbook.R
import cz.ackee.cookbook.databinding.ViewRecipeBinding
import cz.ackee.cookbook.screens.common.ClickCallback
import cz.ackee.cookbook.screens.common.DataBoundListAdapter
import cz.ackee.cookbook.util.AppExecutors
import cz.ackee.cookbook.vo.db.Recipe

class RecipesAdapter(
        appExecutors: AppExecutors,
        private val bindingComponent: DataBindingComponent,
        private val callback: ClickCallback
) : DataBoundListAdapter<Recipe, ViewRecipeBinding>(
        appExecutors,
        object : DiffUtil.ItemCallback<Recipe>() {

            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
                    oldItem == newItem
        }
) {
    override fun createBinding(parent: ViewGroup): ViewRecipeBinding =
            DataBindingUtil.inflate<ViewRecipeBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.view_recipe,
                    parent,
                    false,
                    bindingComponent
            ).apply { clickCallback = callback }

    override fun bind(binding: ViewRecipeBinding, item: Recipe) {
        binding.itemRecipe = item
    }
}