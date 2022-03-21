package com.alzu.android.newsroom.data


import kotlinx.parcelize.RawValue
import java.io.Serializable


data class Source(
     val id: @RawValue Any,
     val name: String
): Serializable