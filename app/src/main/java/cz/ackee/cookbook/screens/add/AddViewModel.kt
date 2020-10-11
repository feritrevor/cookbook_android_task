package cz.ackee.cookbook.screens.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.ackee.cookbook.repository.RecipeRepository
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.vo.CreateRecipeBody
import cz.ackee.cookbook.vo.api.Resource
import cz.ackee.cookbook.vo.db.Recipe
import javax.inject.Inject

class AddViewModel @Inject constructor(
        private val recipeRepository: RecipeRepository
) : ViewModel() {

    var createRecipeResponse: MutableLiveData<Resource<ApiResponse<Recipe>>> = MutableLiveData()

    val name: MutableLiveData<String> = MutableLiveData()
    var duration: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()
    var ingredients: ArrayList<String> = ArrayList()
    var info: MutableLiveData<String> = MutableLiveData()

    fun createRecipe() {
        recipeRepository.createRecipe(
                CreateRecipeBody(
                        name = name.value?.trim() ?: "",
                        description = description.value?.trim() ?: "",
                        info = info.value?.trim() ?: "",
                        duration = duration.value?.toInt() ?: 0,
                        ingredients = ingredients
                ), createRecipeResponse)
    }

}