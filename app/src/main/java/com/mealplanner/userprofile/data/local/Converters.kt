package com.mealplanner.userprofile.data.local

import androidx.room.TypeConverter
import com.mealplanner.userprofile.domain.model.ActivityLevel
import com.mealplanner.userprofile.domain.model.DietGoal
import com.mealplanner.userprofile.domain.model.Gender
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room 类型转换器
 * 
 * 用于处理枚举类型和日期类型的存储
 */
class Converters {
    
    // Gender 枚举转换
    @TypeConverter
    fun fromGender(gender: Gender): String {
        return gender.name
    }
    
    @TypeConverter
    fun toGender(value: String): Gender {
        return Gender.valueOf(value)
    }
    
    // ActivityLevel 枚举转换
    @TypeConverter
    fun fromActivityLevel(activityLevel: ActivityLevel): String {
        return activityLevel.name
    }
    
    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel {
        return ActivityLevel.valueOf(value)
    }
    
    // DietGoal 枚举转换
    @TypeConverter
    fun fromDietGoal(dietGoal: DietGoal): String {
        return dietGoal.name
    }
    
    @TypeConverter
    fun toDietGoal(value: String): DietGoal {
        return DietGoal.valueOf(value)
    }
    
    // LocalDate 转换
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }
    
    // LocalDateTime 转换
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }
}
