package com.example.aplication_aplication_taskman

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RegisterRequest(val name: String ,val mobile: String,val   password: String)
data class LoginRequest(val mobile: String, val password: String)

// match your JSON: { "success": true, "data": { "token": "..." } }
data class LoginResponse(val success: Boolean, val data: LoginData?)
data class LoginData(@SerializedName("token") val token: String?)

// changed: ProfileResponse now wraps ProfileData in "data"
data class ProfileResponse(val data: ProfileData?)
data class ProfileData(
    val id: Int,
    val name: String?,
    val email: String?
)

// Remote model for JSONPlaceholder posts
data class RemoteBlogPost(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface ApiService {
    @POST("register")
    suspend fun login(@Body req: RegisterRequest): Response<LoginResponse>

    @POST("login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @GET("user/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    // fetch blogs from JSONPlaceholder (full URL) -> returns list of RemoteBlogPost
    @GET("https://jsonplaceholder.typicode.com/posts")
    suspend fun getBlogList(): Response<List<RemoteBlogPost>>
}