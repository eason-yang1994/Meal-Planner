package com.mealplanner.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room 类型转换器
 * 
 * 用于转换 LocalDate、LocalDateTime、List<String>、Map<String, Float> 等类型
 * 存储格式：LocalDate/LocalDateTime → Long (Epoch)，List/Map → JSON String
 */
class TypeConverters {
    
    private val gson = Gson()
    
    // ==================== LocalDate (存储为 Epoch Day Long) ====================
    
    /**
     * LocalDate 转 Long (Epoch Day)
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
    
    /**
     * Long (Epoch Day) 转 LocalDate
     */
    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }
    
    // ==================== LocalDateTime (存储为 Epoch Second * 1000) ====================
    
    /**
     * LocalDateTime 转 Long (Epoch Milli)
     */
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.toEpochSecond(java.time.ZoneOffset.UTC)?.times(1000)
    }
    
    /**
     * Long (Epoch Milli) 转 LocalDateTime
     */
    @TypeConverter
    fun toLocalDateTime(epochMilli: Long?): LocalDateTime? {
        return epochMilli?.let { 
            LocalDateTime.ofEpochSecond(it / 1000, 0, java.time.ZoneOffset.UTC)
        }
    }
    
    // ==================== List<String> (存储为 JSON String) ====================
    
    /**
     * List<String> 转 JSON String
     */
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }
    
    /**
     * JSON String 转 List<String>
     */
    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        return json?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, type)
        }
    }
    
    // ==================== Map<String, Float> (存储为 JSON String) ====================
    
    /**
     * Map<String, Float> 转 JSON String
     */
    @TypeConverter
    fun fromStringFloatMap(map: Map<String, Float>?): String? {
        return map?.let { gson.toJson(it) }
    }
    
    /**
     * JSON String 转 Map<String, Float>
     */
    @TypeConverter
    fun toStringFloatMap(json: String?): Map<String, Float>? {
        return json?.let {
            val type = object : TypeToken<Map<String, Float>>() {}.type
            gson.fromJson(it, type)
        }
    }
}
