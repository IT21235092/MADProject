package com.example.madproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(private  val feedList:ArrayList<FeedbackModel>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    private  lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener =clickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.feedback_list_item,parent,false)
        return  ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentfeed= feedList[position]
        holder.tvFeedBackName.text=currentfeed.empAddtional

    }


    override fun getItemCount(): Int {
        return feedList.size
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val tvFeedBackName : TextView =itemView.findViewById(R.id.tvFeedBackName)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}