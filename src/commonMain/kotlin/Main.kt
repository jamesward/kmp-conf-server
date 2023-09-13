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
    Session(
        405587,
        "Compose Multiplatform on iOS",
        """
            An overview of the exciting present and future of Compose Multiplatform, including live demos and how to get started with the latest additions to the multiplatform UI framework built by JetBrains.
        """.trimIndent()
    ),
    Session(
        388863,
        "Level up on Kotlin Multiplatform",
        """
            Kotlin Multiplatform is a new cross-platform technology from JetBrains that is taking the mobile world by storm. Many Android and iOS developers have given this exciting technology a try. But how do you take your skills from building simple apps to more complex ones?
        """.trimIndent()
    ),
    Session(
        391895,
        "Kotlin Multiplatform Mobile for Teams",
        """
            Touchlab has spent the past few years integrating KMM into various apps. The most simple observation we've taken away from that experience is that different teams approach KMM in different ways. What works well for small teams may be different than for larger teams. What works well for greenfield apps may be different than for existing app projects. In this session, we'll cover these different integration approaches. We'll also discuss tooling options, iOS-side API design tips, developer workflows with shared code, and some thoughts on how to present KMM to a team that may not know it needs it (yet!)
        """.trimIndent()
    )
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
