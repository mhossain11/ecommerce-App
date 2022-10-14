package com.faysalh.shopapp.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.faysalh.shopapp.MainActivity
import com.faysalh.shopapp.databinding.ActivityProductDetailsBinding
import com.faysalh.shopapp.roomdb.AppDatabase
import com.faysalh.shopapp.roomdb.ProductDao
import com.faysalh.shopapp.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityProductDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)

    }

    @Suppress("UNCHECKED_CAST")
    private fun getProductDetails(proId:String?) {
        Firebase.firestore.collection("products")
            .document(proId!!).get().addOnSuccessListener {
               val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")
                binding.textView8.text=name
                binding.textView4.text=productSp
                binding.textView9.text=productDesc

                 val sliderList =ArrayList<SlideModel>()
                    for (data in list){
                        sliderList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                    }

                cartAction(proId, name,productSp,it.getString("productCoverImg"))

                binding.imageSlider.setImageList(sliderList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    //Cart

    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {
       val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proId) != null){
            binding.textView10.text = "Go to Cart"

        }else{
            binding.textView10.text = "Add to Cart"
        }


        binding.textView10.setOnClickListener {
            if (productDao.isExit(proId) != null){

                openCart()

            }else{
                addToCart(productDao,proId,name,productSp,coverImg)
            }
        }
    }

    private fun addToCart(
        productDao: ProductDao,
        proId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ) {
        val data = ProductModel( proId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text= "Go to cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}