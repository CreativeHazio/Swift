package com.creativehazio.swift.data.localdb

import androidx.room.TypeConverter
import com.creativehazio.swift.domain.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source) : String {
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : Source {
        return Source(name, name)
    }
}