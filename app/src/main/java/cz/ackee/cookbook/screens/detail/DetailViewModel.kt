package cz.ackee.cookbook.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.ackee.cookbook.repository.RecipeRepository
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.vo.RateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeResponse
import cz.ackee.cookbook.vo.api.Resource
import cz.ackee.cookbook.vo.db.Recipe
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val fetchDetail: MutableLiveData<Boolean> = MutableLiveData()
    private var recId: String = ""
    val starClicked: MutableLiveData<Int> = MutableLiveData()

    var recipeDetailLiveData: LiveData<Resource<Recipe?>> = Transformations.switchMap(fetchDetail) {
        recipeRepository.getOneRecipe(true, recId)
    }

    var rateRecipeResponse: MutableLiveData<Resource<ApiResponse<RateRecipeResponse>>> = MutableLiveData()

    fun fetchDetail(recipeId: String) {
        recId = recipeId
        fetchDetail.value = true
    }

    fun rateRecipe() {
        recipeRepository.rateRecipe(recId, RateRecipeBody(starClicked.value), rateRecipeResponse)
    }


}