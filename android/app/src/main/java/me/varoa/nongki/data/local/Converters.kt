package me.varoa.nongki.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class Converters {
    @TypeConverter
    fun fromList(value: List<Int>) = Json.encodeToString(ListSerializer(serializer()), value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<Int>>(value)
}
