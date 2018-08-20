package com.example

import io.javalin.Javalin
import io.javalin.ApiBuilder.*
import com.google.gson.Gson
import com.example.Usuario
import com.example.User

import java.util.Properties
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

private fun createProducer(brokers: String) : Producer<String, String> {
    val props = Properties()

    props["bootstrap.servers"] = brokers
    props["key.serializer"] = StringSerializer::class.java.canonicalName
    props["value.serializer"] = StringSerializer::class.java.canonicalName

    return KafkaProducer<String, String>(props)
}

fun main(args: Array<String>) {
    val port = System.getenv("PORT") ?: "7000"
    val app = Javalin.start( port.toInt() )

    app.routes {
        get("/") { ctx -> 
            try {
                val users = User().listUsers()
            
                users?.let {
                    ctx.status(200).json(users)
                } ?: ctx.status(404)

            } catch (ex: Exception) {
                ex.printStackTrace()
                ctx.status(500)
            }
        }

        post("/users") { ctx -> 
            val gson = Gson()
            val user = gson.fromJson( ctx.body(), UsuarioModel::class.java )

            if(!user.nome.isBlank()) {
                val producer = createProducer("kafka:9092")

                producer.send(ProducerRecord("userevents", "{ \"type\": \"USER_CREATED\", data: \"${user}\" }"))
            } else {
                ctx.status(400).result("Parametros inválidos.")
            }
        }

        post("/") { ctx ->
            val gson = Gson()
            val user = gson.fromJson( ctx.body(), UsuarioModel::class.java )

            user?.let {
                val output = User().createUser( user )

                output?.let {
                    ctx.status(201).json(output)
                } ?: ctx.status(500)
                
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
