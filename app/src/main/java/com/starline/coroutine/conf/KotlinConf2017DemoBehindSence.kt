package com.starline.coroutine.conf//package com.starline.coroutine
//
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//
//fun main() {
//    runBlocking {
//        GlobalScope.launch {
//            KotlinConf2017Demo.postItem(KotlinConf2017Demo.Item("title"))
//        }
//        delay(4000)
//    }
//}
//
//object KotlinConf2017DemoBehindSence {
//
//    fun postItem(item: Item) {
//        requestToken { token ->
//            createPost(token, item) { post ->
//                processPost(post)
//            }
//        }
//    }
//
//    fun postItem(item: Item, continuation: Continuation) {
//        val stateMachine = if (continuation is ContinuationImpl) {
//            continuation
//        } else {
//            object : ContinuationImpl {
//                fun resume() {
//                    postItem(null, this)
//                }
//            }
//        }
//        when (label) {
//            case 0
//                : {
//                stateMachine.label = 1
//                requestToken(item, stateMachine)
//            }
//
//            case 1
//                : {
//                stateMachine.label = 2
//                createPost(token, item, stateMachine)
//            }
//
//            case 2
//                : {
//                stateMachine.label = 3
//                processPost(post, stateMachine)
//            }
//        }
//    }
//
//    private fun requestToken(callback: (String) -> Unit) {
//        callback.invoke("token")
//    }
//
//    private fun createPost(token: String, item: Item, callback: (Post) -> Unit) {
//        callback.invoke(Post(item, token))
//    }
//
//    private fun processPost(post: Post) {
//        println("process successful")
//    }
//
//    data class Item(val title: String)
//
//    data class Post(val item: Item, val token: String)
//
//
//}