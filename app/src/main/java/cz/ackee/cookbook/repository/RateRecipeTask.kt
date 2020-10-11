package cz.ackee.cookbook.repository

import androidx.lifecycle.MutableLiveData
import cz.ackee.cookbook.api.ApiService
import cz.ackee.cookbook.repository.common.RestTask
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.util.api.SupplierWithException
import cz.ackee.cookbook.vo.RateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeResponse
import cz.ackee.cookbook.vo.api.Resource
import retrofit2.Response

class RateRecipeTask(
        recipeId: String,
        rateRecipeBody: RateRecipeBody,
        liveData: MutableLiveData<Resource<ApiResponse<RateRecipeResponse>>>,
        private val apiService: ApiService
) : RestTask<RateRecipeResponse>(
        liveData,
        object : SupplierWithException<Response<RateRecipeResponse>> {
            override fun get(): Response<RateRecipeResponse> =
                    apiService.rateRecipe(recipeId, rateRecipeBody).execute()
        }
)