package com.faysalh.shopapp.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert
     suspend fun insertProduct(productModel: ProductModel)

    @Delete
     suspend fun deleteProduct(productModel: ProductModel)

    @Query("SELECT * From products")
      fun getAllProduct():LiveData<List<ProductModel>>

    @Query("SELECT * From products where productId= :Id ")
      fun isExit(Id:String):ProductModel
}