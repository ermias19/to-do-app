package com.example.schedule.DAOs

import androidx.room.*
import com.example.schedule.models.Task
import java.util.*

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<Task>)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE uid = :uid")
    fun getByID(uid: UUID): Task

    @Query("SELECT * FROM task WHERE start_date BETWEEN :startDate AND :endDate ORDER BY start_date")
    fun getAllBetweenDates(startDate: Date, endDate: Date): List<Task>

    @Query("SELECT * from task WHERE (lower(title) LIKE :query)")
    fun getAllStartingWith(query: String): List<Task>

}