package com.example.madproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class EmpAdapter(private val empList: ArrayList<PaaymentModel>) :
    RecyclerView.Adapter<EmpAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.tvpayer.text = currentEmp.payer
        holder.tvjobType.text = currentEmp.jobType
        holder.tvEmpName.text = currentEmp.paymentAmount
        holder.tvPayMethod.text = currentEmp.paymentMethod
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvjobType : TextView = itemView.findViewById(R.id.tvjobType)
        val tvEmpName : TextView = itemView.findViewById(R.id.tvEmpName)
        val tvpayer : TextView = itemView.findViewById(R.id.tvpayer)
        val tvPayMethod : TextView = itemView.findViewById(R.id.tvPayMethod)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}