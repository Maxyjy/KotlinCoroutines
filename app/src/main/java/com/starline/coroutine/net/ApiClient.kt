package com.starline.coroutine.net

import com.starline.coroutine.model.ApiState
import com.starline.coroutine.model.CommentModel
import com.starline.coroutine.model.UserModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * ApiClient
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class ApiClient {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    private var mApi: Api? = null

    init {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        val okHttpClient = clientBuilder.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mApi = retrofit.create(Api::class.java)
    }

    suspend fun getComment(postId: Int): ApiState<CommentModel> {
        val comment = mApi?.getComment(postId)
        delay(500)
        return ApiState.success(comment)
    }

    suspend fun getUser(): ApiState<UserModel> {
        val user = mApi?.getUser()
        delay(1200)
        return ApiState.success(user)
    }

}