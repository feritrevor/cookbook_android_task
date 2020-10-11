package cz.ackee.cookbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.ackee.cookbook.api.ApiService
import cz.ackee.cookbook.db.RecipeDao
import cz.ackee.cookbook.util.AppExecutors
import cz.ackee.cookbook.util.NetworkBoundResource
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.vo.CreateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeResponse
import cz.ackee.cookbook.vo.api.Resource
import cz.ackee.cookbook.vo.db.Recipe
import javax.inject.Inject

class RecipeRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val apiService: ApiService,
        private val recipeDao: RecipeDao
) {
    fun loadRecipes(shouldFetch: Boolean, limit: Int, offset: Int): LiveData<Resource<List<Recipe>?>> {
        return object : NetworkBoundResource<List<Recipe>?, List<Recipe>?>(appExecutors) {
            var itemsDb: MutableList<Recipe>? = null

            override fun saveCallResult(item: List<Recipe>?) {
                if (item == null) return
                var existsInDb: Boolean
                if (itemsDb != null) {
                    for (recipeRest in item) {
                        existsInDb = false
                        for (recipe in itemsDb ?: emptyList<Recipe>()) {
                            if (recipe.id == recipeRest.id) {
                                recipeRest.description = recipe.description
                                recipeRest.info = recipe.info
                                recipeRest.ingredients = recipe.ingredients
                                recipeDao.updateItem(recipeRest)
                                existsInDb = true
                                break
                            }
                        }
                        if (!existsInDb) {
                            recipeDao.insertItem(recipeRest)
                        }
                    }
                } else {
                    recipeDao.insertItems(item)
                }
            }

            override fun shouldFetch(data: List<Recipe>?): Boolean {
                itemsDb = data?.toMutableList()
                return shouldFetch
            }

            override fun loadFromDb() = recipeDao.loadAllRecipes()

            override fun createCall() = apiService.getRecipes(limit, offset)
        }.asLiveData()
    }

    fun getOneRecipe(shouldFetch: Boolean, id: String): LiveData<Resource<Recipe?>> {
        return object : NetworkBoundResource<Recipe?, Recipe?>(appExecutors) {

            override fun saveCallResult(item: Recipe?) {
                if (item == null) return
                recipeDao.updateItem(item)
            }

            override fun shouldFetch(data: Recipe?): Boolean {
                return shouldFetch
            }

            override fun loadFromDb() = recipeDao.getRecipeWithId(id)

            override fun createCall() = apiService.getRecipeDetail(id)
        }.asLiveData()
    }

    fun rateRecipe(
            recipeId: String,
            rateRecipeBody: RateRecipeBody,
            liveData: MutableLiveData<Resource<ApiResponse<RateRecipeResponse>>>
    ) {
        val task = RateRecipeTask(
                recipeId,
                rateRecipeBody,
                liveData,
                apiService
        )
        appExecutors.networkIO().execute(task)
    }

    fun createRecipe(
            createRecipeBody: CreateRecipeBody,
            liveData: MutableLiveData<Resource<ApiResponse<Recipe>>>
    ) {
        val task = CreateRecipeTask(
                createRecipeBody,
                liveData,
                apiService
        )
        appExecutors.networkIO().execute(task)
    }
}