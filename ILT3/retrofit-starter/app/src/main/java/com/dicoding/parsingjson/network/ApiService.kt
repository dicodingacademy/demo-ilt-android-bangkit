package com.dicoding.parsingjson.network

import com.dicoding.parsingjson.model.ResponseUser
import java.io.File

// TODO : [4] Create API Service to Connect API by End Point
interface ApiService {
    //get list users with query
    fun getListUsers(page: String): ResponseUser

    //get list user by id using path
    fun getUser(id: String): ResponseUser

    //post user using field x-www-form-urlencoded
    fun createUser(
        name: String,
        job: String
    ): ResponseUser

    //upload file using multipart
    fun updateUser(
        file: File,
        data: Map<String, File>
    ): ResponseUser
}