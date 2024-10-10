package com.inmobixpress.repository

import com.inmobixpress.dao.DeviceDao
import com.inmobixpress.dao.DeviceDaoImpl
import com.inmobixpress.model.Device

class DeviceRepository(
    private val dao: DeviceDao = DeviceDaoImpl
) {
    suspend fun getAll(): List<Device> = dao.getAll()
    suspend fun findById(id: Int): Device? = dao.findById(id)
    suspend fun insert(device: Device) = dao.insert(device)
    suspend fun update(id: Int, device: Device) = dao.update(id, device)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}