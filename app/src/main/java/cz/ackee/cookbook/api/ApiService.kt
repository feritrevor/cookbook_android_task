package cz.ackee.cookbook.api

import androidx.lifecycle.LiveData
import cz.ackee.cookbook.util.api.ApiResponse
import cz.ackee.cookbook.vo.CreateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeBody
import cz.ackee.cookbook.vo.RateRecipeResponse
import cz.ackee.cookbook.vo.db.Recipe
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("recipes")
    fun getRecipes(
            @Query("limit") limit: Int,
            @Query("offset") offset: Int
    ): LiveData<ApiResponse<List<Recipe>?>>

    @GET("recipes/{recipeId}")
    fun getRecipeDetail(
            @Path("recipeId") recipeId: String
    ): LiveData<ApiResponse<Recipe?>>

    @POST("recipes/{recipeId}/ratings")
    fun rateRecipe(
            @Path("recipeId") recipeId: String,
            @Body rateRecipeBody: RateRecipeBody
    ): Call<RateRecipeResponse>

    @POST("recipes")
    fun createRecipe(
            @Body rateRecipeBody: CreateRecipeBody
    ): Call<Recipe>


}