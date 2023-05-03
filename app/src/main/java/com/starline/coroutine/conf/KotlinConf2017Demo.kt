package com.starline.coroutine.conf

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        KotlinConf2017Demo.postItem(KotlinConf2017Demo.Item("title"))
    }
}

object KotlinConf2017Demo {

    suspend fun postItem(item: Item) {
        val token = requestToken()
        val post = createPost(token, item)
        processPost(post)
    }

    private suspend fun requestToken(): String {
        delay(1000)
        return "token"
    }

    private suspend fun createPost(token: String, item: Item): Post {
        delay(1000)
        return Post(item, token)
    }

    private suspend fun processPost(post: Post) {
        delay(1000)
        println("process successful")
    }

    data class Item(val title: String)

    data class Post(val item: Item, val token: String)


}