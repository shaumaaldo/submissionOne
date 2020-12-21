package com.shaumaaldo.submissionone.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.shaumaaldo.submissionone.BuildConfig
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.adapter.ItemUserFollowAdapter
import com.shaumaaldo.submissionone.model.UserFollow
import org.json.JSONArray

class FollowerFragment : Fragment() {
    private lateinit var rvItemFollower: RecyclerView
    private lateinit var rlLoadingFollower: RelativeLayout
    val listFollower = ArrayList<UserFollow>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val username = arguments?.getString(ARG_USERNAME)
        val view = inflater.inflate(R.layout.fragment_follower, container, false)
        rvItemFollower = view.findViewById(R.id.rv_follower_list)
        rlLoadingFollower = view.findViewById(R.id.rl_loading_follower)

        getFollowerList(username)
        return view
    }

    companion object {
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String?) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private fun getFollowerList(username: String?) {
        AndroidNetworking.get("https://api.github.com/users/{username}/followers")
            .addPathParameter("username", username)
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    // do anything with response
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)

                        val name = jsonObject.getString("login")
                        val image = jsonObject.getString("avatar_url")
                        val userFollowNew = UserFollow(name, image)
                        listFollower.add(userFollowNew)
                        Log.e("listFollower: ", listFollower.toString())
                        adapterReady()
                        rlLoadingFollower.visibility = View.GONE
                    }
                }

                override fun onError(error: ANError) {
                    Log.e("onErrorFollower: ", error.toString())
                    // handle error
                    rlLoadingFollower.visibility = View.GONE
                }
            })
    }

    private fun adapterReady() {
        rvItemFollower.layoutManager = LinearLayoutManager(activity?.applicationContext)
        val adapterUser = ItemUserFollowAdapter(listFollower)
        rvItemFollower.adapter = adapterUser

        adapterUser.setOnItemClickCallback(object : ItemUserFollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollow) {
                Toast.makeText(activity?.applicationContext, data.nameLogin, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}