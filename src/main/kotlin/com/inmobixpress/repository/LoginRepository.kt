package com.inmobixpress.repository

import com.inmobixpress.dao.UserDao
import com.inmobixpress.dao.UserDaoImpl
import com.inmobixpress.model.User

class LoginRepository(
    private val dao: UserDao = UserDaoImpl
) {
    suspend fun login(username: String, password: String): User? {
        val user = dao.getAll().firstOrNull { it.username == username && it.password == password }
        return user
    }
}