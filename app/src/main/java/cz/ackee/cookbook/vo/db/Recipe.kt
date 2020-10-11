package cz.ackee.cookbook.vo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
        @PrimaryKey val id: String,
        var name: String,
        var duration: Int,
        var score: Double,
        var description: String?,
        var ingredients: ArrayList<String>?,
        var info: String?

)