package com.example.madproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class JobAdapter(private val jobList: ArrayList<JobModel>) :
    RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.tvJobId.text = currentJob.jobType
        holder.tvEmpName.text = currentJob.experience
        holder.tvJobPay.text = currentJob.payment

    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvJobId : TextView = itemView.findViewById(R.id.tvProduct)
        val tvEmpName : TextView = itemView.findViewById(R.id.tvStore)
        val tvJobPay : TextView = itemView.findViewById(R.id.tvEmpName)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}