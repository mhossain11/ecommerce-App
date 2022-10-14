package com.faysalh.shopapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao():ProductDao

    companion object{
        var db :AppDatabase?=null
        val NAME_DATABASE = "ShopApp"

        @Synchronized
        fun getInstance(context: Context):AppDatabase{

            if (db ==null){
                db = Room.databaseBuilder(
                    context,AppDatabase::class.java, NAME_DATABASE
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return db!!




        }
    }

}