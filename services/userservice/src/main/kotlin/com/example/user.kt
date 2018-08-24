package com.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.dao.*
import java.sql.*
import java.util.Properties

data class UsuarioModel(var ID: Int, var nome: String, var email: String, var host: String)

object Users : IntIdTable() {
    val nome = varchar("nome", 250).index()
    val host = varchar("host", 200)
	val email = varchar("email", 150)
}

class Usuario(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Usuario>(Users)

    var nome by Users.nome
    var host by Users.host
	var email by Users.email
}

class User {
    private val connectionString = "jdbc:mysql://root:test@userdb:3306/UserDB?useSSL=false"

    fun listUsers() : List<UsuarioModel>? {

        Database.connect(connectionString, driver = "com.mysql.cj.jdbc.Driver")

        var output : MutableList<UsuarioModel>? = null

        transaction {
            addLogger(StdOutSqlLogger)

            create(Users)

            val list = Usuario.all()

            if ( !list.empty() ) {
                output = mutableListOf<UsuarioModel>()

                list.forEach {
                    output?.add( UsuarioModel( it.id.value, it.nome, it.email, it.host ) )
                }
            }
        }

        return output
    }

    fun createUser( u: UsuarioModel) : UsuarioModel? {
        Database.connect(connectionString, driver = "com.mysql.cj.jdbc.Driver")

        return transaction {
            addLogger(StdOutSqlLogger)

            create(Users)

            try {
                val output = Usuario.new {
                    nome = u.nome
                    host = System.getenv("HOSTNAME") ?: "localhost"
					email = u.email
                }

                return@transaction UsuarioModel( output.id.value, output.nome, output.email, output.host )
            } catch (e: Exception) {
                e.printStackTrace()
                return@transaction null
            } catch (sq: SQLException) {
                sq.printStackTrace()
                return@transaction null
            }
        }
    }
}
