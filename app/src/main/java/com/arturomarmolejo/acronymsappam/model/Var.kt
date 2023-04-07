package com.arturomarmolejo.acronymsappam.model


import com.google.gson.annotations.SerializedName

data class Var(
    @SerializedName("freq")
    val freq: Int = 0,
    @SerializedName("lf")
    val lf: String = "",
    @SerializedName("since")
    val since: Int = 0
)