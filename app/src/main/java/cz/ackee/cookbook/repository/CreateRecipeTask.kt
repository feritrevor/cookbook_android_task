package cz.ackee.cookbook.repository

import androidx.lifecycle.MutableLiveData
import cz.ackee.cookbook.api.ApiService
import cz.ackee.cookbook.repository.common.RestTask
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.util.api.SupplierWithException
import cz.ackee.cookbook.vo.CreateRecipeBody
import cz.ackee.cookbook.vo.api.Resource
import cz.ackee.cookbook.vo.db.Recipe
import retrofit2.Response

class CreateRecipeTask(
        createRecipeBody: CreateRecipeBody,
        liveData: MutableLiveData<Resource<ApiResponse<Recipe>>>,
        private val apiService: ApiService
) : RestTask<Recipe>(
        liveData,
        object : SupplierWithException<Response<Recipe>> {
            override fun get(): Response<Recipe> =
                    apiService.createRecipe(createRecipeBody).execute()
        }
)