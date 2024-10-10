package com.inmobixpress.repository

import com.inmobixpress.dao.ImageDao
import com.inmobixpress.dao.ImageDaoImpl
import com.inmobixpress.model.Image

class ImageRepository(
    private val dao: ImageDao = ImageDaoImpl
) {
    suspend fun getAll(): List<Image> = dao.getAll()
    suspend fun findById(id: Int): Image? = dao.findById(id)
    suspend fun insert(image: Image) = dao.insert(image)
    suspend fun update(id: Int, image: Image) = dao.update(id, image)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}