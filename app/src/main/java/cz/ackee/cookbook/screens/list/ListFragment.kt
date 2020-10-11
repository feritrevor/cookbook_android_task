package cz.ackee.cookbook.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.ackee.cookbook.AppConfig
import cz.ackee.cookbook.R
import cz.ackee.cookbook.binding.FragmentDataBindingComponent
import cz.ackee.cookbook.databinding.FragmentListBinding
import cz.ackee.cookbook.di.Injectable
import cz.ackee.cookbook.screens.common.ClickCallback
import cz.ackee.cookbook.util.AppExecutors
import cz.ackee.cookbook.vo.api.Status
import cz.ackee.cookbook.vo.db.Recipe
import javax.inject.Inject

class ListFragment : Fragment(), Injectable, ClickCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var listModel: ListViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.listToolbar.apply {
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_global_addFragment)
                    }
                }
                true
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.rwRecipes.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecipesAdapter(
                    appExecutors,
                    dataBindingComponent,
                    this@ListFragment
            )
            // handler for fetching next portion of recipes
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        listModel.fetchingDataEnabled = true
                        listModel.fetchData()
                    }
                }

            })
        }

        listModel.allRecipesLiveData.observe(viewLifecycleOwner, Observer { recipes ->
            binding.resource = recipes
            if (recipes.status != Status.LOADING) {
                if (listModel.fetchingDataEnabled) {
                    listModel.listRecipes.addAll(recipes.data as List<Recipe>)
                }
                (binding.rwRecipes.adapter as RecipesAdapter).apply {
                    submitList(listModel.listRecipes)
                    notifyDataSetChanged()
                }
                // if new recipes fetched scroll up a little
                if (!(listModel.listRecipes.size <= AppConfig.LIMIT_FOR_RECIPES_FETCH || (recipes.data as List<Recipe>).isEmpty())) {
                    binding.rwRecipes.smoothScrollBy(0, 200)
                }
            }
        })

        if (listModel.fetchingDataEnabled) {
            listModel.fetchData()
        }
    }

    override fun onClick(view: View) {
        val recipeId = view.tag as String?
        if (recipeId != null) {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(recipeId)
            findNavController().navigate(action)
        }
    }

    override fun onPause() {
        super.onPause()
        listModel.fetchingDataEnabled = false
    }
}