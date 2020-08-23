package com.sandy.chalkboard.ui.view.adapter

import android.R.attr.data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandy.chalkboard.R
import com.sandy.chalkboard.coroutines.data.model.Class
import com.sandy.chalkboard.coroutines.data.model.School
import com.sandy.chalkboard.utils.CommonMethods
import kotlinx.android.synthetic.main.data_items.view.*


class ClassesAdapter(private val clazz: ArrayList<Class>) :
    RecyclerView.Adapter<ClassesAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(clazz: Class) {
            itemView.apply {
                tvTitle.text = clazz.title
                tvEventDate.text = "On ${CommonMethods.getDate(clazz.eventDate, "dd MMM")}"
                tvCreateDate.text = CommonMethods.getDate(clazz.createdDate, "dd MMM")
                tvClassName.text = "Class ${clazz.standard}th"
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
        return clazz.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(clazz[position])
    }
    fun addUsers(clazz: List<Class>) {
        this.clazz.apply {
            clear()
            addAll(clazz)
        }

    }
    fun removeItem(position: Int) {
        this.clazz.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Class, position: Int) {
        this.clazz.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): ArrayList<Class> {
        return clazz
    }
}