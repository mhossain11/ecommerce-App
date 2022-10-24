package com.faysalh.shopapp.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.faysalh.shopapp.R
import com.faysalh.shopapp.adapter.AllOrderAdapter
import com.faysalh.shopapp.databinding.FragmentMoreBinding
import com.faysalh.shopapp.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MoreFragment : Fragment() {
lateinit var binding: FragmentMoreBinding
lateinit var list:ArrayList<AllOrderModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        list= ArrayList()

        val preferences = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId",preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)
                }
                binding.recycleView.adapter=AllOrderAdapter(requireContext(),list)
        }

        return binding.root
    }

}