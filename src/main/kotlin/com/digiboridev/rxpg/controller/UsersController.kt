package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.security.AppAuthentication
import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.data.dto.PersonalUserInfo
import com.digiboridev.rxpg.data.dto.PublicUserInfo
import com.digiboridev.rxpg.data.repository.UsersRepository
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.web.bind.annotation.*


@Tag(name = "Users", description = "Users endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController()
@RequestMapping("/api/users")
class UsersController(private val usersRepository: UsersRepository) {

    @GetMapping("/me")
    suspend fun me(auth: AppAuthentication): PersonalUserInfo {
        val userId = auth.id;
        return usersRepository.findPersonalUserInfoById(userId) ?: throw ResourceException.notFound("User")
    }

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: String): PublicUserInfo {
        return usersRepository.findPublicUserById(id) ?: throw ResourceException.notFound("User")
    }

    @GetMapping()
    suspend fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Flow<PublicUserInfo> {
        return usersRepository.findAllPublicUsersBy(PageRequest.of(page, size))
    }

    @GetMapping("/search/{query}")
    suspend fun searchUsers(@PathVariable query: String): Flow<PublicUserInfo> {
        val criteria = TextCriteria.forDefaultLanguage().matching(query)
        return usersRepository.findAllBy(criteria)
    }
}



