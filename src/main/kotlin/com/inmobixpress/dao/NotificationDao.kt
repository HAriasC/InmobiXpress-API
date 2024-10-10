package com.inmobixpress.dao

interface NotificationDao {
    suspend fun sendMessageToOwner()
    suspend fun sendMessageToClient()
    suspend fun sendAlertToOwner()
    suspend fun sendAlertToClient()
}

object NotificationDaoImpl : NotificationDao {
    override suspend fun sendMessageToOwner() {

    }

    override suspend fun sendMessageToClient() {

    }

    override suspend fun sendAlertToOwner() {

    }

    override suspend fun sendAlertToClient() {

    }

}