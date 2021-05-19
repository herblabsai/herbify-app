package com.herblabs.herbifyapp.view.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.herblabs.herbifyapp.R

class WelcomeAdapter(private val welcomeModel: List<WelcomeModel>): RecyclerView.Adapter<WelcomeAdapter.WelcomeAdapterViewHolder>() {
    inner class WelcomeAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        private val tvDesription = view.findViewById<TextView>(R.id.tv_description)
        private val imgIntro = view.findViewById<ImageView>(R.id.img_intro)

        fun bind(welcomeModel: WelcomeModel){
            tvTitle.text = welcomeModel.title
            tvDesription.text = welcomeModel.description
            imgIntro.setImageResource(welcomeModel.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeAdapterViewHolder {
        return WelcomeAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.welcome_item_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WelcomeAdapterViewHolder, position: Int) {
        holder.bind(welcomeModel[position])
    }

    override fun getItemCount(): Int = welcomeModel.size
}