import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.awaitCancellation
import kotlinx.serialization.Serializable


@Serializable
data class Session(val id: Int, val title: String, val description: String)

val sessions = listOf(
    Session(
        389291,
        "Kotlin Multiplatform Conversions at Android Jetpack Scale",
        """
                This is a case study of how we converted several Jetpack libraries to Kotlin Multiplatform as part of our ongoing experimentation with the multiplatform technology.
            """.trimIndent()
    ),
)

fun main() = SuspendApp {
    resourceScope {
        server(CIO, port = 8080) {
            install(ContentNegotiation) {
                json()
            }

            routing {
                get("/") {
                    call.respond(sessions)
                }
            }
        }
        awaitCancellation()
    }
}
