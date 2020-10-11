package cz.ackee.cookbook.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.ackee.cookbook.R
import cz.ackee.cookbook.databinding.FragmentDetailBinding
import cz.ackee.cookbook.di.Injectable
import cz.ackee.cookbook.util.api.ApiErrorResponse
import cz.ackee.cookbook.util.api.ApiSuccessResponse
import cz.ackee.cookbook.vo.api.Status
import javax.inject.Inject


class DetailFragment : Fragment(), Injectable, DetailFragmentCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var detailModel: DetailViewModel

    private lateinit var binding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    private var canShowDialog = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickCallback = this


        binding.toolbar.apply {
            inflateMenu(R.menu.menu_detail)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_global_addFragment)
                    }
                }
                true
            }
            setNavigationIcon(R.drawable.ic_back_white)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = detailModel

        detailModel.recipeDetailLiveData.observe(viewLifecycleOwner, Observer { recipe ->
            binding.resource = recipe
            if (recipe.status != Status.LOADING) {
                binding.recipe = recipe.data
            }
        })

        detailModel.rateRecipeResponse.observe(viewLifecycleOwner, Observer { response ->
            if (canShowDialog) {
                binding.resource = response
                if (response.status != Status.LOADING) {
                    canShowDialog = false
                    when (response.data) {
                        is ApiSuccessResponse -> {
                            context?.let {
                                AlertDialog.Builder(it)
                                        .setTitle(R.string.rating_success_title)
                                        .setMessage(getString(R.string.rating_success_description, response.data.body.score.toString()))
                                        .setNeutralButton(R.string.ok) { dialog, _ ->
                                            dialog.dismiss()
                                            detailModel.fetchDetail(args.recipeId)
                                        }
                                        .setCancelable(false)
                                        .show()
                            }
                        }
                        is ApiErrorResponse -> {
                            context?.let {
                                AlertDialog.Builder(it)
                                        .setTitle(R.string.rating_error_title)
                                        .setMessage(R.string.rating_error_description)
                                        .setNeutralButton(R.string.ok) { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .setCancelable(false)
                                        .show()
                            }
                        }
                    }
                }
            }
        })

        detailModel.fetchDetail(args.recipeId)
    }

    override fun onStarClick(id: Int) {
        canShowDialog = true
        detailModel.starClicked.value = id
        detailModel.rateRecipe()
    }
}