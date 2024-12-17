import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import io.javalin.http.Cookie
import io.javalin.http.SameSite
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import org.agileSoftDev.domain.User

class CookieController {

    // Save User to Cookie with HTTP cookies (server-side only)
    fun saveToCookieStore(ctx: Context, user: User) {
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(user)

        // URL-encode the cookie value to ensure it's safe for transmission
        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())

        // Create the cookie with the encoded value
        val cookie = Cookie(
            "user",            // Cookie name
            encodedUserJson,   // URL-encoded cookie value
            maxAge = 3600,     // maxAge in seconds (1 hour)
            path = "/",        // Path for the cookie (default is "/")
            isHttpOnly = true, // Make the cookie HttpOnly (not accessible via JavaScript)
            secure = false,    // Whether the cookie is secure (use true for HTTPS)
            sameSite = SameSite.NONE // SameSite attribute to control cross-origin requests
        )

        // Set the cookie in the response
        ctx.cookie(cookie)
    }

    // Retrieve User from Cookie (HTTP cookie) on server side
    fun getFromCookieStore(ctx: Context, key: String): User? {
        val encodedUserJson = ctx.cookie(key) // Retrieve the cookie value from the HTTP request
        if (encodedUserJson == null) return null

        // URL-decode the cookie value before deserializing
        val decodedUserJson = URLDecoder.decode(encodedUserJson, StandardCharsets.UTF_8.toString())

        // Deserialize the user object from the decoded cookie string
        return jacksonObjectMapper().readValue(decodedUserJson)
    }
}
