package com.herblabs.herbifyapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herblabs.herbifyapp.data.source.firebase.model.Step
import com.herblabs.herbifyapp.databinding.ItemStepRecipeBinding

class StepsAdapter(var stepsList: List<Step>): RecyclerView.Adapter<StepsAdapter.StepsViewHolder>() {
    inner class StepsViewHolder(val binding: ItemStepRecipeBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val binding = ItemStepRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StepsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val item = stepsList[position]
        with(holder){
            with(item){
                val number = position + 1
                binding.tvContent.text = caption
                binding.tvNumber.text = number.toString()
            }
        }
    }

    override fun getItemCount(): Int = stepsList.size
}