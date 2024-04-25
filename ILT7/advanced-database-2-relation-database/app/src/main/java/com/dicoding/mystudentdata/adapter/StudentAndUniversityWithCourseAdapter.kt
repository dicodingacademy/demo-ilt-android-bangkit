package com.dicoding.mystudentdata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mystudentdata.database.StudentAndUniversityWithCourse
import com.dicoding.mystudentdata.database.StudentWithCourse
import com.dicoding.mystudentdata.databinding.ItemStudentBinding

class StudentAndUniversityWithCourseAdapter :
    ListAdapter<StudentAndUniversityWithCourse, StudentAndUniversityWithCourseAdapter.WordViewHolder>(
        WordsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentAndUniversityWithCourse) {
            binding.tvItemUniversity.text = data.studentAndUniversity.university?.name
            binding.tvItemUniversity.visibility = View.VISIBLE
            binding.tvItemName.text = data.studentAndUniversity.student.name
            val arrayCourse = arrayListOf<String>()
            data.course.forEach {
                arrayCourse.add(it.name)
            }
            binding.tvItemCourse.text = arrayCourse.joinToString(separator = ", ")
            binding.tvItemCourse.visibility = View.VISIBLE
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<StudentAndUniversityWithCourse>() {
        override fun areItemsTheSame(
            oldItem: StudentAndUniversityWithCourse,
            newItem: StudentAndUniversityWithCourse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: StudentAndUniversityWithCourse,
            newItem: StudentAndUniversityWithCourse
        ): Boolean {
            return oldItem.studentAndUniversity.student.name == newItem.studentAndUniversity.student.name
        }
    }
}