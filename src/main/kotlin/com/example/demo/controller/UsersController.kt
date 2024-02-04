package com.example.demo.controller

import com.example.demo.core.AppAuthentication
import com.example.demo.core.exceptions.UserExceptions
import com.example.demo.dto.PersonalUserInfo
import com.example.demo.dto.PublicUserInfo
import com.example.demo.repository.UsersRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.web.bind.annotation.*


@RestController()
@RequestMapping("/api/users")
class UsersController(private val usersRepository: UsersRepository) {


    @GetMapping("/me")
    fun me(auth: AppAuthentication): PersonalUserInfo {
        val userId = auth.id;
        return usersRepository.findPersonalUserInfoById(userId) ?: throw UserExceptions.NotFound()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): PublicUserInfo {
        return usersRepository.findPublicUserById(id) ?: throw UserExceptions.NotFound()
    }

    @GetMapping()
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<PublicUserInfo> {
        return usersRepository.findAllPublicUsersBy(PageRequest.of(page, size))
    }

    @GetMapping("/search/{query}")
    fun searchUsers(@PathVariable query: String): List<PublicUserInfo> {
        val criteria = TextCriteria.forDefaultLanguage().matching(query)
        return usersRepository.findAllBy(criteria)
    }
}



