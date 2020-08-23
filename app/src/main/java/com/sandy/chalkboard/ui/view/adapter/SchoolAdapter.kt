package com.sandy.chalkboard.ui.view.adapter

import android.R.attr.data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandy.chalkboard.R
import com.sandy.chalkboard.coroutines.data.model.School
import com.sandy.chalkboard.utils.CommonMethods
import kotlinx.android.synthetic.main.data_items.view.*


class SchoolAdapter(private val school: ArrayList<School>) :
    RecyclerView.Adapter<SchoolAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(school: School) {
            itemView.apply {
                tvTitle.text = school.title
                tvEventDate.text = "On ${CommonMethods.getDate(school.eventDate, "dd MMM")}"
                tvCreateDate.text = CommonMethods.getDate(school.createdDate, "dd MMM")
                tvClassName.text = "Class ${school.standard}th"
                tvClassName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle, 0, 0, 0)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.data_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return school.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(school[position])
    }
    fun addUsers(school: List<School>) {
        this.school.apply {
            clear()
            addAll(school)
        }

    }
    fun removeItem(position: Int) {
        this.school.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: School, position: Int) {
        this.school.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): ArrayList<School> {
        return school
    }
}