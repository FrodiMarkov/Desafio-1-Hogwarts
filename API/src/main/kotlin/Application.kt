import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    try {
        configureSerialization()
        configureRouting()
    } catch (e: Exception) {
        println("Error arrancando el servidor: ${e.message}")
        e.printStackTrace()
    }
}