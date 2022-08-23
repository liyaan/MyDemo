package com.liyaan.test

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("id") var id: Long = 0,
    @SerializedName("title") var title: String = "",
    @SerializedName("desc") var desc: String = "",
    @SerializedName("url") var url: String = ""
)
