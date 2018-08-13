package com.example

data class Usuario(var ID: Long, var nome: String)

class User {
    fun listUsers() : List<Usuario>? {
        return listOf( Usuario(1, "Rafael"), Usuario(2, "Taly") )
    }
    fun createUser() : Usuario? {
        return null
    }
}