package com.arturomarmolejo.acronymsappam.model


import com.google.gson.annotations.SerializedName

data class Lf(
    @SerializedName("freq")
    val freq: Int = 0,
    @SerializedName("lf")
    val lf: String = "",
    @SerializedName("since")
    val since: Int = 0,
    @SerializedName("vars")
    val vars: List<Var> = listOf()
)