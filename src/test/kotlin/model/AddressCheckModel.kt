package model

data class Postcode(val postcode: Int, val cities: List<String>)

data class Streets(val postcode: Int, val city: String, val streets: List<String>)