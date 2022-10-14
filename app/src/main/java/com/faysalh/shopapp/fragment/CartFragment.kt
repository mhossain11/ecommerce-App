package com.faysalh.shopapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.faysalh.shopapp.R
import com.faysalh.shopapp.activity.AddressActivity
import com.faysalh.shopapp.activity.CategoryActivity
import com.faysalh.shopapp.activity.ProductDetailsActivity
import com.faysalh.shopapp.adapter.CartAdapter
import com.faysalh.shopapp.databinding.FragmentCartBinding
import com.faysalh.shopapp.roomdb.AppDatabase
import com.faysalh.shopapp.roomdb.ProductModel


class CartFragment : Fragment() {
    lateinit var binding : FragmentCartBinding
    lateinit var list :ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false )
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()
        list = ArrayList()
        dao.getAllProduct().observe(requireActivity()){
            binding.cartRecycler.adapter=CartAdapter(requireContext(),it)

            list.clear()
            for (data in it){
                list.add(data.productId)
            }

            totalCast(it)
        }

        return binding.root
    }

    private fun totalCast(data: List<ProductModel>?) {
            var total = 0
        for (item in data!!){
            total += item.productSp!!.toInt()

        }
        binding.textView11.text="Total item in cart is ${data.size}"
        binding.textView12.text="Total Cost: $total"

        binding.checkout.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            val b= Bundle()
            b.putStringArrayList("productIds",list)
            b.putString("totalCost",total.toString())
            intent.putExtras(b)
            startActivity(intent)

        }

    }

}