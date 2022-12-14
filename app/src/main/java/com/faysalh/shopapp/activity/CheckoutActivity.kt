package com.faysalh.shopapp.activity

import android.content.ContentValues.TAG
import  android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.faysalh.shopapp.MainActivity
import com.faysalh.shopapp.R
import com.faysalh.shopapp.databinding.ActivityCheckoutBinding
import com.faysalh.shopapp.roomdb.AppDatabase
import com.faysalh.shopapp.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class CheckoutActivity() : AppCompatActivity(),PaymentResultListener {
    lateinit var binding: ActivityCheckoutBinding
    private val TAG= CheckoutActivity::class.qualifiedName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_YMpRNrRlG6VDeZ")
        val price = intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name", "PKART")
            options.put("description", "Best Ecommerce App")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#673AB7")
            options.put("currency", "BDT")
            options.put("amount", (price!!.toInt() * 100).toString()) //pass amount in currency subunits
            options.put("prefill.email", "faysalHossain@gmail.com")
            options.put("prefill.contact", "1687477579")
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()

        uploadData()

    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for (currentId in id!!) {
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {


                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId))
                }

                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)
            }

    }

    private fun saveData(name: String?, price: String?, productId: String) {

        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number","")!!
        Log.e(TAG, "saveData: ${preferences.getString("number","")!!}" )

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

    }

   override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
    }

}



