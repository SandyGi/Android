package com.sandy.chalkboard.ui.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sandy.chalkboard.R
import com.sandy.chalkboard.coroutines.data.api.ApiHelper
import com.sandy.chalkboard.coroutines.data.api.RetrofitBuilder
import com.sandy.chalkboard.coroutines.data.model.Class
import com.sandy.chalkboard.ui.base.ViewModelFactory
import com.sandy.chalkboard.ui.view.adapter.ClassesAdapter
import com.sandy.chalkboard.ui.view.adapter.SwipeToDeleteCallback
import com.sandy.chalkboard.ui.viewmodel.DataViewModel
import com.sandy.chalkboard.utils.Status
import kotlinx.android.synthetic.main.fragment_class.*

class ClassFragment : Fragment() {
    private lateinit var viewModel: DataViewModel
    private lateinit var adapterClass: ClassesAdapter
    private lateinit var mContenxt: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_class, container, false)!!

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null) {
            mContenxt = context
        };
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupViewModel()
        setClassObservers()
        enableSwipeToDeleteAndUndo()
    }

    private fun setupUI() {
        rvClass.layoutManager = LinearLayoutManager(activity)
        adapterClass = ClassesAdapter(arrayListOf())
        rvClass.addItemDecoration(
            DividerItemDecoration(
                rvClass.context,
                (rvClass.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvClass.adapter = adapterClass

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(DataViewModel::class.java)
    }


    private fun retrieveClassList(clazz: List<Class>) {
        adapterClass.apply {
            addUsers(clazz)
            notifyDataSetChanged()
        }
    }

    private fun setClassObservers() {

        viewModel.getClasses().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rvClass.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { classes -> retrieveClassList(classes) }
                    }
                    Status.ERROR -> {
                        rvClass.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        rvClass.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(mContenxt) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition

                val item: Class = adapterClass.getData().get(position)
                adapterClass.removeItem(position)
                val snackbar: Snackbar = Snackbar
                    .make(
                        coordinateLayout,
                        "School was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO", View.OnClickListener {
                    adapterClass.restoreItem(item, position)
                    rvClass.scrollToPosition(position)
                })
                adapterClass.notifyDataSetChanged()
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }


        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(rvClass)
    }
}