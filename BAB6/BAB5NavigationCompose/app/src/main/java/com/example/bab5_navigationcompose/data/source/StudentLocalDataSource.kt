package com.example.bab5_navigationcompose.data.source

import com.example.bab5_navigationcompose.data.model.Student

class StudentLocalDataSource {

    private val students = listOf(
        Student(2026, "Alya Putri", "Teknik Informatika", 2026),
        Student(2027, "Bima Pratama", "Teknik Informatika", 2026),
        Student(2028, "Citra Lestari", "Teknik Informatika", 2026),
        // Dua data mahasiswa baru yang ditambahkan
        Student(2029, "Putri Maharani", "Sistem Informasi", 2025),
        Student(2030, "Rafi Pratama", "Pendidikan Teknologi Informasi", 2024)
    )

    fun getStudentById(id: Int): Student? =
        students.find { it.id == id }

    fun getStudents(): List<Student> = students
}