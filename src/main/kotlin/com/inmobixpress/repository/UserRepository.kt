package com.inmobixpress.repository

import com.inmobixpress.dao.UserDao
import com.inmobixpress.dao.UserDaoImpl
import com.inmobixpress.model.User

class UserRepository(
    private val dao: UserDao = UserDaoImpl
) {
    suspend fun getAll(): List<User> = dao.getAll()
    suspend fun findById(id: Int): User? = dao.findById(id)
    suspend fun insert(user: User) = dao.insert(user)
    suspend fun update(id: Int, user: User) = dao.update(id, user)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}