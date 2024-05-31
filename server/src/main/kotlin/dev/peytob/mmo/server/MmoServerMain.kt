package dev.peytob.mmo.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MmoServerMain

fun main(args: Array<String>) {
    SpringApplication.run(MmoServerMain::class.java, *args)
}
