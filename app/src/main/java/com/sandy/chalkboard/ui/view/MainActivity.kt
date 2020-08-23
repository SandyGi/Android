package com.sandy.chalkboard.ui.view

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.sandy.chalkboard.R
import com.sandy.chalkboard.coroutines.data.api.ApiHelper
import com.sandy.chalkboard.coroutines.data.api.RetrofitBuilder
import com.sandy.chalkboard.coroutines.data.model.Class
import com.sandy.chalkboard.coroutines.data.model.School
import com.sandy.chalkboard.ui.base.ViewModelFactory
import com.sandy.chalkboard.ui.view.adapter.ClassesAdapter
import com.sandy.chalkboard.ui.view.adapter.SchoolAdapter
import com.sandy.chalkboard.ui.view.adapter.SwipeToDeleteCallback
import com.sandy.chalkboard.ui.viewmodel.DataViewModel
import com.sandy.chalkboard.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: DataViewModel
    private lateinit var adapterSchool: SchoolAdapter
    private lateinit var adapterClass: ClassesAdapter
    private var isClasses = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupUI()
        setSchoolObservers(false)
        enableSwipeToDeleteAndUndo()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        val switchOnOffItem = menu.findItem(R.id.menu_switch)
        switchOnOffItem.setActionView(R.layout.switch_layout)
        val switchOnOff: SwitchCompat =
            switchOnOffItem.getActionView().findViewById(R.id.scSchoolClass)
        switchOnOff.setOnCheckedChangeListener { buttonView, isChecked ->
            isClasses = isChecked
            if (isChecked){
                setSchoolObservers(true)
                Toast.makeText(this, "Classes loading...", Toast.LENGTH_LONG).show()
            }else{
                setSchoolObservers(false)
                Toast.makeText(this, "Schools loading...", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    private fun setupUI() {
        rvSchool.layoutManager = LinearLayoutManager(this)
        adapterSchool = SchoolAdapter(arrayListOf())
        adapterClass = ClassesAdapter(arrayListOf())
        rvSchool.addItemDecoration(
            DividerItemDecoration(
                rvSchool.context,
                (rvSchool.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvSchool.adapter = adapterSchool
        rvSchool.adapter = adapterClass
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(DataViewModel::class.java)
    }

    private fun setSchoolObservers(isClass : Boolean) {
        if (isClass){
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
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        rvSchool.visibility = View.GONE
                    }
                }
            }
        })}else{
            viewModel.getClasses().observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            rvSchool.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            resource.data?.let { classes -> retrieveClassList(classes) }
                        }
                        Status.ERROR -> {
                            rvSchool.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            rvSchool.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    private fun retrieveClassList(clazz: List<Class>) {
        adapterClass.apply {
            addUsers(clazz)
            notifyDataSetChanged()
        }
    }
        private fun retrieveSchoolList(schools: List<School>) {
            adapterSchool.apply {
                addUsers(schools)
                notifyDataSetChanged()
            }
        }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                if (isClasses) {
                    val item: Class = adapterClass.getData().get(position)
                    adapterClass.removeItem(position)
                    val snackbar: Snackbar = Snackbar
                        .make(
                            coordinateLayout,
                            "Class was removed from the list.",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.setAction("UNDO", View.OnClickListener {
                        adapterClass.restoreItem(item, position)
                        rvSchool.scrollToPosition(position)
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }else{
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
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }

            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(rvSchool)
    }
}