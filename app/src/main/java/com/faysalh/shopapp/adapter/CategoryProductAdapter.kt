package com.faysalh.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faysalh.shopapp.activity.ProductDetailsActivity
import com.faysalh.shopapp.databinding.ItemCategoryProductLayoutBinding
import com.faysalh.shopapp.model.AddProductModel

class CategoryProductAdapter(val context: Context,val list:ArrayList<AddProductModel>):RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewModel>() {
   inner class CategoryProductViewModel(val binding:ItemCategoryProductLayoutBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewModel {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryProductViewModel(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewModel, position: Int) {
        val data = list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.textView.text=list[position].productName
        holder.binding.textView3.text=list[position].productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}