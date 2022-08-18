package com.example.hooliganchallenge.models

import java.io.Serializable
import java.util.*

data class Events(val id: Int, val title: String, val subtitle: String, val date: Date, val imageUrl: String, val videoUrl: String?) :
    Serializable
