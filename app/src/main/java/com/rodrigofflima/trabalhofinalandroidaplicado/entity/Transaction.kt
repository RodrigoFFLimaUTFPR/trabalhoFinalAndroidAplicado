package com.rodrigofflima.trabalhofinalandroidaplicado.entity

data class Transaction(
    val id: Int,
    val type: String,
    val detail: String,
    val amount: Double,
    val date: String
)
