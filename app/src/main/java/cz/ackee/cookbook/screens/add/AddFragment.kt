package cz.ackee.cookbook.screens.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.ackee.cookbook.R
import cz.ackee.cookbook.databinding.FragmentAddBinding
import cz.ackee.cookbook.di.Injectable
import cz.ackee.cookbook.util.UiUtil
import cz.ackee.cookbook.util.api.ApiErrorResponse
import cz.ackee.cookbook.util.api.ApiSuccessResponse
import cz.ackee.cookbook.vo.api.Status
import javax.inject.Inject

class AddFragment : Fragment(), Injectable, AddFragmentCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddViewModel

    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickCallback = this

        binding.toolbarList.inflateMenu(R.menu.menu_main)
        binding.toolbarList.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    UiUtil.hideKeyboard(activity)
                    validateInputs()
                }
            }
            true
        }
        binding.toolbarList.setNavigationIcon(R.drawable.ic_back_blue)
        binding.toolbarList.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        for (i in 1..2) {
            val ingredient1 = LayoutInflater.from(context).inflate(R.layout.view_ingredients_input, null)
            binding.ingredientsWrapper.addView(ingredient1)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AddViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.createRecipeResponse.observe(viewLifecycleOwner, Observer { response ->
            binding.resource = response
            if (response.status != Status.LOADING) {
                when (response.data) {
                    is ApiSuccessResponse -> {
                        context?.let {
                            AlertDialog.Builder(it)
                                    .setTitle(R.string.rating_success_title)
                                    .setMessage(R.string.create_new_recipe_success_description)
                                    .setNeutralButton(R.string.ok) { dialog, _ ->
                                        dialog.dismiss()
                                        findNavController().navigate(R.id.action_addFragment_to_listFragment)
                                    }
                                    .setCancelable(false)
                                    .show()
                        }
                    }
                    is ApiErrorResponse -> {
                        when {
                            response.data.errorMessage.contains("Name must obtain Ackee") -> {
                                UiUtil.showAlertDialog(R.string.rating_error_title, R.string.create_new_recipe_error_no_ackee, context)
                            }
                            response.data.errorMessage.contains("Recipe with this name already exists") -> {
                                UiUtil.showAlertDialog(R.string.rating_error_title, R.string.create_new_recipe_error_name_already_exists, context)
                            }
                            else -> {
                                UiUtil.showAlertDialog(R.string.rating_error_title, R.string.rating_error_description, context)
                            }
                        }
                    }
                }
            }
        })


    }

    override fun onAddIngredientClick() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_ingredients_input, null)
        binding.ingredientsWrapper.addView(view)
    }

    private fun prepareIngredients() {
        viewModel.ingredients.clear()
        for (i in 0..binding.ingredientsWrapper.childCount) {
            val view = binding.ingredientsWrapper.getChildAt(i)
            if (view is EditText) {
                val text = view.text.toString().trim()
                if (text.isNotEmpty()) {
                    viewModel.ingredients.add(text)
                }
            }
        }
    }

    private fun validateInputs() {
        var cantContinue = false
        if (viewModel.name.value?.trim().isNullOrEmpty()) {
            binding.textInputName.error = getString(R.string.create_new_recipe_input_error)
            cantContinue = true
        }
        if (viewModel.info.value?.trim().isNullOrEmpty()) {
            binding.textInputIntro.error = getString(R.string.create_new_recipe_input_error)
            cantContinue = true
        }
        if (viewModel.description.value?.trim().isNullOrEmpty()) {
            binding.textInputProcess.error = getString(R.string.create_new_recipe_input_error)
            cantContinue = true
        }
        if (viewModel.duration.value?.trim().isNullOrEmpty()) {
            binding.textInputTime.error = getString(R.string.create_new_recipe_input_error)
            cantContinue = true
        }
        if (!cantContinue) {
            prepareIngredients()
            viewModel.createRecipe()
        }
    }
}