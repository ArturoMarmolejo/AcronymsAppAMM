package com.arturomarmolejo.acronymsappam.model


import com.google.gson.annotations.SerializedName

data class AcromineItem(
    @SerializedName("lfs")
    val lfs: List<Lf> = listOf(),
    @SerializedName("sf")
    val sf: String = ""
)