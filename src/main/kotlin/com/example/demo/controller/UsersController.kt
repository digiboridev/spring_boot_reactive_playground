package com.example.demo.controller

import com.example.demo.core.AppAuthentication
import com.example.demo.core.exceptions.UserExceptions
import com.example.demo.dto.PersonalUserInfo
import com.example.demo.dto.PublicUserInfo
import com.example.demo.repository.UsersRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController()
@RequestMapping("/api/users")
class UsersController(private val usersRepository: UsersRepository) {


    @GetMapping("/me")
    fun me(auth: AppAuthentication): PersonalUserInfo {
        val userId = auth.id;
        val user = usersRepository.findById(userId).get()
        return PersonalUserInfo(user)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): PublicUserInfo {
        val user = usersRepository.findById(id).orElseThrow { UserExceptions.NotFound()}
        return PublicUserInfo(user)
    }


    @GetMapping()
    fun getAllUsers(): List<PublicUserInfo> {
        return usersRepository.findAll().map { PublicUserInfo(it) }
    }
}


