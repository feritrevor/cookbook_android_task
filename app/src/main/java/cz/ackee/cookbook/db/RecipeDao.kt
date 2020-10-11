package cz.ackee.cookbook.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import cz.ackee.cookbook.vo.db.Recipe

@Dao
interface RecipeDao : DatabaseOperationDao<Recipe> {

    @Query("SELECT * FROM Recipe WHERE `offset` == :offset")
    fun loadAllRecipes(offset: Int): LiveData<List<Recipe>?>

    @Query("SELECT * FROM Recipe WHERE id == :id")
    fun getRecipeWithId(id: String): LiveData<Recipe?>
}