package com.example.loadmore

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var studentIndex = 0
    private val studentList = ArrayList<Student?>()

    private var handler = Handler()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addStudents(20)

        layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        adapter = DataAdapter(studentList, recyclerView)
        recyclerView.adapter = adapter

        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                studentList.add(null)
                adapter.notifyItemInserted(studentList.size - 1)

                handler.postDelayed({
                    studentList.removeAt(studentList.size - 1)
                    adapter.notifyItemRemoved(studentList.size)

                    addStudents(20)

                    adapter.notifyItemInserted(studentList.size)
                    adapter.setLoaded()
                }, 2000)
            }
        })
    }

    private fun addStudents(count: Int) {
        for (i in (studentIndex + 1)..(studentIndex + count)) {
            studentList.add(Student("Student$i", "s$i@gmail.com"))
        }
        studentIndex += count
    }
}