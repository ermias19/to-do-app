package com.example.schedule.DAOs

import android.util.Log
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let{ Date(it) }

    @TypeConverter
    fun dateToTimestamp(value: Date?) = value?.time

    @TypeConverter
    fun uuidToString(value: UUID?) = value?.toString()

    @TypeConverter
    fun fromString(value: String?) = value?.let { UUID.fromString(it) }

    @TypeConverter
    fun listToSting(value: List<Pair<String, Boolean>>?) =
        value?.map {
            "${it.first}||${it.second}"
        }?.joinToString("^^")

    @TypeConverter
    fun listFromString(value: String?): List<Pair<String, Boolean>>{
        val result = mutableListOf<Pair<String, Boolean>>()
        value?.split("^^")?.forEach {
            val item = it.split("||")
            if (item.size == 2) {
                result.add(Pair(item.first(), item[1] == "true"))
            }
        }

        return result.toList()
    }
}