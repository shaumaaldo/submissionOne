package com.shaumaaldo.submissionone.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
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

class FollowingFragment : Fragment() {
    private lateinit var rvItemFollowing: RecyclerView
    private lateinit var rlLoadingFollowing: RelativeLayout
    val listFollower = ArrayList<UserFollow>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val username = arguments?.getString(ARG_USERNAME)

        val view = inflater.inflate(R.layout.fragment_following, container, false)
        rvItemFollowing = view.findViewById(R.id.rv_following_list)
        rlLoadingFollowing = view.findViewById(R.id.rl_loading_following)

        getFollowingList(username)
        return view
    }

    companion object {
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String?) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private fun getFollowingList(username: String?) {
        AndroidNetworking.get("https://api.github.com/users/{username}/following")
            .addPathParameter("username", username)
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    Log.e("onResponseFollowing: ", response.toString())
                    // do anything with response
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)

                        val name = jsonObject.getString("login")
                        val image = jsonObject.getString("avatar_url")
                        val userFollowNew = UserFollow(name, image)
                        listFollower.add(userFollowNew)
                        Log.e("listFollower: ", listFollower.toString())
                        adapterReady()
                        rlLoadingFollowing.visibility = View.GONE
                    }
                }

                override fun onError(error: ANError) {
                    Log.e("onErrorFollowing: ", error.toString())
                    // handle error
                    rlLoadingFollowing.visibility = View.GONE
                }
            })
    }

    private fun adapterReady() {
        rvItemFollowing.layoutManager = LinearLayoutManager(activity?.applicationContext)
        val adapterUser = ItemUserFollowAdapter(listFollower)
        rvItemFollowing.adapter = adapterUser

        adapterUser.setOnItemClickCallback(object : ItemUserFollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollow) {
                Toast.makeText(activity?.applicationContext, data.nameLogin, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}