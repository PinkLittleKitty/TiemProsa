package com.santyfisela.tiemprosa.data

data class Quote(
    val hour: Int,
    val minute: Int,
    val text: String,
    val author: String,
    val book: String
)