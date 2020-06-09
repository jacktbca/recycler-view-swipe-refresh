package com.example.loadmore

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var studentIndex = 0
    private val studentList = ArrayList<Student?>()

    private var handler = Handler()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initStudents()

        layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        adapter = DataAdapter(studentList, recyclerView)
        recyclerView.adapter = adapter

        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                studentList.add(null)
                recyclerView.post { adapter.notifyItemInserted(studentList.size - 1) }

                handler.postDelayed({
                    studentList.removeAt(studentList.size - 1)
                    adapter.notifyItemRemoved(studentList.size)

                    addStudents(20)

                    adapter.notifyItemInserted(studentList.size)
                    adapter.setLoaded()
                }, 2000)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            handler.postDelayed({
                initStudents()
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
    }

    private fun initStudents() {
        studentList.clear()
        addStudents(20)
    }

    private fun addStudents(count: Int) {
        for (i in (studentIndex + 1)..(studentIndex + count)) {
            studentList.add(Student("Student$i", "student-$i@gmail.com"))
        }
        studentIndex += count
    }
}