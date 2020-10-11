package cz.ackee.cookbook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.ackee.cookbook.vo.db.Recipe

@Database(
        entities = [Recipe::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CookbookDb : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}