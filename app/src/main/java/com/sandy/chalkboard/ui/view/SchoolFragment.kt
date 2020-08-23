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
import com.sandy.chalkboard.coroutines.data.model.School
import com.sandy.chalkboard.ui.base.ViewModelFactory
import com.sandy.chalkboard.ui.view.adapter.SchoolAdapter
import com.sandy.chalkboard.ui.view.adapter.SwipeToDeleteCallback
import com.sandy.chalkboard.ui.viewmodel.DataViewModel
import com.sandy.chalkboard.utils.Status
import kotlinx.android.synthetic.main.fragment_school.*

class SchoolFragment : Fragment() {
    private lateinit var viewModel: DataViewModel
    private lateinit var adapterSchool: SchoolAdapter
    private lateinit var mContenxt: Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null) {
            mContenxt = context
        };
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_school, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupViewModel()
        setSchoolObservers()
        enableSwipeToDeleteAndUndo()
    }

    private fun setupUI() {
        rvSchool.layoutManager = LinearLayoutManager(activity)
        adapterSchool = SchoolAdapter(arrayListOf())
        rvSchool.addItemDecoration(
            DividerItemDecoration(
                rvSchool.context,
                (rvSchool.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvSchool.adapter = adapterSchool

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(DataViewModel::class.java)
    }

    private fun setSchoolObservers() {

        viewModel.getSchools().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rvSchool.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { schools -> retrieveSchoolList(schools) }
                    }
                    Status.ERROR -> {
                        rvSchool.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        rvSchool.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun retrieveSchoolList(schools: List<School>) {
        adapterSchool.apply {
            addUsers(schools)
            notifyDataSetChanged()
        }
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(mContenxt) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition

                val item: School = adapterSchool.getData().get(position)
                adapterSchool.removeItem(position)
                val snackbar: Snackbar = Snackbar
                    .make(
                        coordinateLayout,
                        "School was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO", View.OnClickListener {
                    adapterSchool.restoreItem(item, position)
                    rvSchool.scrollToPosition(position)
                })
                adapterSchool.notifyDataSetChanged()
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }


        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(rvSchool)
    }
}