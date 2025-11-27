package com.example.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "collectors_albums_table",
    foreignKeys = [
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["collectorId"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Album::class,
            parentColumns = ["albumId"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["collectorId"]),
        androidx.room.Index(value = ["albumId"])
    ])
data class CollectorAlbum (
    @PrimaryKey val collector_albumId:Int,
    val price:Int,
    val status:String,
    val collectorId: Int,
    val albumId: Int
)