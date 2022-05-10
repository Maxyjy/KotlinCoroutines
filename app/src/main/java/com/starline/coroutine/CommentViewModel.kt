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


    fun getComment(postId: Int) {
        //Network call will takes time, so we set the value to loading state
        commentFlow.value = ApiState.loading()
        //Network call will takes time, so has to be run in the background thread.
        //use viewModelScope to limit the call available only when viewModel still alive.
        viewModelScope.launch {
            try {
                //if api call is succeeded, set the state to success, and set data to data received from api
                commentFlow.value = client.getComment(postId)
            } catch (e: Exception) {
                e.printStackTrace()
                //ignore CancellationException
                if (e is CancellationException) {
                    return@launch
                }
                //If any error occurs like 404 or unknown host, it will be catch in here, and we set the state to error, so the ui will show some info
                commentFlow.value = ApiState.error(e.message.toString())
            }
        }
    }

    fun getUser() {
        //Network call will takes time, so we set the value to loading state
        usersFlow.value = ApiState.loading()
        //Network call will takes time, so has to be run in the background thread.
        //use viewModelScope to limit the call available only when viewModel still alive.
        viewModelScope.launch {
            try {
                //if api call is succeeded, set the state to success, and set data to data received from api
                usersFlow.value = client.getUser()
            } catch (e: Exception) {
                e.printStackTrace()
                //ignore CancellationException
                if (e is CancellationException) {
                    return@launch
                }
                //If any error occurs like 404 or unknown host, it will be catch in here, and we set the state to error, so the ui will show some info
                usersFlow.value = ApiState.error(e.message.toString())
            }
        }
    }

    fun serialRequest() {
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