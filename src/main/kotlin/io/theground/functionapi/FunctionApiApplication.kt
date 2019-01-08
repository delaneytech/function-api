package io.theground.functionapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FunctionApiApplication

fun main(args: Array<String>) {
	runApplication<FunctionApiApplication>(*args)
}

