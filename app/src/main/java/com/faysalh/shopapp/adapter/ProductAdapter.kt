package com.faysalh.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faysalh.shopapp.activity.ProductDetailsActivity
import com.faysalh.shopapp.databinding.LayoutProductItemBinding
import com.faysalh.shopapp.model.AddProductModel

class ProductAdapter(val context: Context,val list:ArrayList<AddProductModel>):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: LayoutProductItemBinding)
       : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView3)
        holder.binding.textView5.text=data.productName
        holder.binding.textView6.text=data.productCategory
        holder.binding.textView7.text= data.productMrp

        holder.binding.button3.text=data.productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}