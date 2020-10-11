package cz.ackee.cookbook.vo

data class CreateRecipeBody(
        var name: String,
        var duration: Int,
        var description: String?,
        var ingredients: ArrayList<String>?,
        var info: String?
)