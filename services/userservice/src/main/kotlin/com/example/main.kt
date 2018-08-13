package com.example

import io.javalin.Javalin
import io.javalin.ApiBuilder.*
import com.google.gson.Gson
import com.example.Usuario
import com.example.User

fun main(args: Array<String>) {
    val port = System.getenv("PORT") ?: "7000"
    val app = Javalin.start( port.toInt() )

    app.routes {
        get("/") { ctx -> 
            val users = User().listUsers()
            
            users?.let {
                ctx.status(200).json(users)
            } ?: ctx.status(404)
        }

        post("/") { ctx ->
            val gson = Gson()
            val user = gson.fromJson( ctx.body(), Usuario::class.java )

            user?.let {
                user.ID = 800
                ctx.json( user ).status(201)
            } ?: ctx.status(400).result("entrada inválida")
        }

        post("/upload") { ctx ->
            if( ctx.contentType() != "text/csv") {
                ctx.status(400).result("Conteudo invalido")
                return@post
            }

            ctx.status( 202 ).result("Arquivo recebido")
        }
    }
}
