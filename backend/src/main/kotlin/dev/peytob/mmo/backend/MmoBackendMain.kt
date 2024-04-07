package dev.peytob.mmo.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MmoBackendMain

fun main(args: Array<String>) {
    SpringApplication.run(MmoBackendMain::class.java, *args)
}
