package cz.ackee.cookbook.db

import androidx.room.*

@Dao
interface DatabaseOperationDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(itemList: List<T>)

    @Update
    fun updateItem(item: T)

    @Update
    fun updateItems(items: List<T>)

    @Delete
    fun deleteItem(item: T)

    @Delete
    fun deleteItems(items: List<T>)
}