package com.starline.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starline.coroutine.model.ApiState
import com.starline.coroutine.model.CommentModel
import com.starline.coroutine.model.UserModel
import com.starline.coroutine.net.ApiClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

/**
 *
 * ViewModel
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class CommentViewModel : ViewModel() {

    private val client = ApiClient()

    val usersFlow = MutableStateFlow<ApiState<UserModel>>(ApiState.loading())

    val commentFlow = MutableStateFlow<ApiState<CommentModel>>(ApiState.loading())


    fun serialRequest() {
        //reset ui to loading status
        usersFlow.value = ApiState.loading()
        commentFlow.value = ApiState.loading()

        viewModelScope.launch {
            val user = client.getUser()
            usersFlow.emit(user)

            val comment = client.getComment(user.data?.id!!)
            commentFlow.emit(comment)
        }
    }

    fun parallelRequest() {
        //reset ui to loading status
        usersFlow.value = ApiState.loading()
        commentFlow.value = ApiState.loading()

        viewModelScope.launch {
            usersFlow.emit(client.getUser())
        }
        viewModelScope.launch {
            commentFlow.emit(client.getComment(1))
        }
    }

    fun parallelMergeRequest() {
        //reset ui to loading status
        usersFlow.value = ApiState.loading()
        commentFlow.value = ApiState.loading()

        viewModelScope.launch {
            //get user
            val user = async {
                client.getUser()
            }
            //get comment
            val comment = async {
                client.getComment(1)
            }

            //execute when user and comment request are both finished
            merge(user.await(), comment.await())
        }
    }

    private fun merge(user: ApiState<UserModel>, comment: ApiState<CommentModel>) {
        viewModelScope.launch {
            usersFlow.emit(user)
            commentFlow.emit(comment)
        }
    }

}