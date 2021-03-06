package cz.ackee.cookbook.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.ackee.cookbook.AppConfig
import cz.ackee.cookbook.repository.RecipeRepository
import cz.ackee.cookbook.vo.api.Resource
import cz.ackee.cookbook.vo.db.Recipe
import javax.inject.Inject

class ListViewModel @Inject constructor(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val fetchData: MutableLiveData<Boolean> = MutableLiveData()

    val listRecipes: ArrayList<Recipe> = ArrayList()
    var fetchedRecipesCount = 0
    var fetchingDataEnabled = true


    var allRecipesLiveData: LiveData<Resource<List<Recipe>?>> = Transformations.switchMap(fetchData) {
        recipeRepository.loadRecipes(true, AppConfig.LIMIT_FOR_RECIPES_FETCH, fetchedRecipesCount)
    }

    fun fetchData() {
        fetchedRecipesCount = listRecipes.size
        fetchData.value = true
    }

}