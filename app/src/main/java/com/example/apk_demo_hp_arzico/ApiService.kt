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

// Task model for task list
data class TaskItem(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: Int?,
    val description: String?,
    val priority: Int?,
    val status: String?,
    val flag: String?,
    val dtimefrmt: String?,
    val project: String?,
    val phase: String?,
    @SerializedName("during_live")
    val duringLive: Int?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class TaskResponse(val data: List<TaskItem>?)

data class PaginatedTaskResponse(
    val data: List<TaskItem>,
    val links: Links?,
    val meta: Meta?
)

data class Links(
    val first: String?,
    val last: String?,
    val prev: String?,
    val next: String?
)

data class Meta(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val path: String?,
    val per_page: Int,
    val to: Int,
    val total: Int,
    val links: List<LinkItem>?
)

data class LinkItem(
    val url: String?,
    val label: String,
    val active: Boolean
)

// Single task response and task time model
data class TaskTimeItem(
    val id: Int,
    val title: String?,
    val body: Int?,
    val description: Long?,
    val priority: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class SingleTaskData(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: Int?,
    val description: String?,
    val priority: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val pdate: String?,
    val status: String?,
    @SerializedName("task_times") val taskTimes: List<TaskTimeItem>?
)

data class SingleTaskResponse(val data: SingleTaskData?)

interface ApiService {
    @POST("register")
    suspend fun login(@Body req: RegisterRequest): Response<LoginResponse>

    @POST("login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @GET("user/profile/index")
    suspend fun getProfile(): Response<ProfileResponse>

    // fetch tasks from taskman API with pagination
    @GET("user/task/index")
    suspend fun getTasks(): Response<PaginatedTaskResponse>

    @GET("user/task/index")
    suspend fun getTasksByPage(@retrofit2.http.Query("page") page: Int): Response<PaginatedTaskResponse>

    // get single task by id
    @GET("user/task/{id}")
    suspend fun getTaskById(@retrofit2.http.Path("id") id: Int): Response<SingleTaskResponse>

    // fetch blogs from JSONPlaceholder (full URL) -> returns list of RemoteBlogPost
    @GET("https://jsonplaceholder.typicode.com/posts")
    suspend fun getBlogList(): Response<List<RemoteBlogPost>>
}