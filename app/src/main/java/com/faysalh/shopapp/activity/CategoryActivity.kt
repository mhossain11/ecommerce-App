package com.faysalh.shopapp.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.faysalh.shopapp.R
import com.faysalh.shopapp.adapter.CategoryAdapter
import com.faysalh.shopapp.adapter.CategoryProductAdapter
import com.faysalh.shopapp.adapter.ProductAdapter
import com.faysalh.shopapp.databinding.ActivityCategoryBinding
import com.faysalh.shopapp.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    lateinit var binding :ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProduct(intent.getStringExtra("cat"))
    }

    private fun getProduct(category: String?) {

        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.recycleView.adapter = CategoryProductAdapter(this,list)


            }

    }
}