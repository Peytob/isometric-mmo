package dev.peytob.mmo.backend.configuration.security.filter

import dev.peytob.mmo.backend.service.UserCrudService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

private val REGISTRATION_ENDPOINT_MATCHER: RequestMatcher = AntPathRequestMatcher("/**/register")

/**
 * Blocks all requests from users, that not registered in MMO service. Or just register users by '/register' in kk?...
 */
class RegisteredOnlyUsersFilter(
    private val userCrudService: UserCrudService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (request.userPrincipal != null && userCrudService.isUserExistsByExternalId(request.userPrincipal.name)) {
            filterChain.doFilter(request, response)
            return
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not registered in this service")
    }

    override fun shouldNotFilter(request: HttpServletRequest) = REGISTRATION_ENDPOINT_MATCHER.matches(request)
}
