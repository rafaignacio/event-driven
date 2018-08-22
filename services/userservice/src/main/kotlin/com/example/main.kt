package com.example

import io.javalin.Javalin
import io.javalin.ApiBuilder.*
import com.google.gson.Gson
import com.example.Usuario
import com.example.User
import org.apache.log4j.*

import java.util.Properties
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

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

    BasicConfigurator.configure()

    println("listening on port ${port}")

    app.routes {
        get("/") { ctx ->
            ctx.status(200).result("OKDOK")
        }

        get("/users") { ctx ->
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
                try {
                    val producer = createProducer("kafka1:9093")

                    // val output = producer.send(ProducerRecord("userevent", "{ \"type\": \"USER_CREATED\", data: {\"${user}\"} }")).get()
                    val output = producer.send(ProducerRecord("userevent1", "TEST")).get()
                    producer.close()

                    if(output.hasOffset()) {
                        ctx.status(201).json(output)
                    } else {
                        ctx.status(500)
                    }
                } catch (ex: Exception) {
                    println(ex.message)
                    ex.printStackTrace()
                    ctx.status(500).json(ex)
                }
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

    app.exception(Exception::class.java) { e, ctx ->
        println(e.message)
        e.printStackTrace()
    }
}
