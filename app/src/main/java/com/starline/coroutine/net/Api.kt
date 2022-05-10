package com.starline.coroutine.net

import com.starline.coroutine.model.CommentModel
import com.starline.coroutine.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
interface Api {

    @GET("/comments/1")
    suspend fun getComment(@Query("postId") postId: Int): CommentModel

    @GET("/users/1")
    suspend fun getUser(): UserModel


}