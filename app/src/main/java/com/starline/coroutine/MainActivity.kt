package com.starline.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.starline.coroutine.databinding.ActivityMainBinding
import com.starline.coroutine.model.Status
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * MainActivity
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "KotlinCoroutinesDemo"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this)[CommentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSingleNetworkRequest.setOnClickListener {
            viewModel.getComment(1)
            viewModel.getUser()
        }

        binding.btnSerialNetworkRequest.setOnClickListener {
            viewModel.serialRequest()
        }

        binding.btnParallelNetworkRequest.setOnClickListener {
            viewModel.parallelRequest()
        }

        binding.btnParallelMergeNetworkRequest.setOnClickListener {
            viewModel.parallelMergeRequest()
        }

        collect()
        viewModel.getComment(1)
        viewModel.getUser()
    }

    /**
     * single network request
     */
    private fun collect() {
        //collect comment result flow
        lifecycleScope.launch {
            viewModel.commentFlow.collect { it ->
                Log.d(TAG, "[Comment] Status:${it.status.name} Content:${it.data?.body}")
                when (it.status) {
                    //show loading progress bar
                    Status.LOADING -> {
                        binding.progressbarCommentLoading.visibility = View.VISIBLE
                        binding.tvComment.text = "Loading..."
                    }
                    //show comment data
                    Status.SUCCESS -> {
                        binding.progressbarCommentLoading.visibility = View.GONE
                        binding.tvComment.text = it.data?.body
                    }
                    //show error message
                    Status.ERROR -> {
                        binding.progressbarCommentLoading.visibility = View.GONE
                        binding.tvComment.text = it.message
                    }
                }
            }
        }

        //collect user result flow
        lifecycleScope.launch {
            viewModel.usersFlow.collect{
                Log.d(TAG, "[User] Status:${it.status.name} Content:${it.data?.name}")
                when (it.status) {
                    //show loading progress bar
                    Status.LOADING -> {
                        binding.progressbarUserLoading.visibility = View.VISIBLE
                        binding.tvUser.text = "Loading..."
                    }
                    //show comment data
                    Status.SUCCESS -> {
                        binding.progressbarUserLoading.visibility = View.GONE
                        binding.tvUser.text = it.data?.name
                    }
                    //show error message
                    Status.ERROR -> {
                        binding.progressbarUserLoading.visibility = View.GONE
                        binding.tvUser.text = it.message
                    }
                }
            }
        }
    }



}