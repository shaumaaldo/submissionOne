package com.shaumaaldo.submissionone.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.shaumaaldo.submissionone.BuildConfig
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.adapter.ItemUserFollowAdapter
import com.shaumaaldo.submissionone.model.UserFollow
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {
    private lateinit var tbSearch: Toolbar
    private lateinit var svUser: SearchView
    private lateinit var rvSearchResult: RecyclerView
    val listFollower = ArrayList<UserFollow>()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        AndroidNetworking.initialize(applicationContext)

        title = getString(R.string.title_search)
        tbSearch = findViewById(R.id.tb_search)
        svUser = findViewById(R.id.sv_search_user)
        rvSearchResult = findViewById(R.id.rv_search_result)
        progressBar = findViewById(R.id.pb_search)

        setSupportActionBar(tbSearch)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        svUser.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        svUser.queryHint = resources.getString(R.string.search_hint)
        svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                searchUser(query)
                svUser.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.act_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.act_fav) {
            Toast.makeText(this, "Favourite", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    fun searchUser(username: String?) {
        AndroidNetworking.get("https://api.github.com/search/users?q={username}")
            .addPathParameter("username", username)
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {

                    val usersSearch = response.getJSONArray("items")
                    Log.e("usersSearch: ", usersSearch.toString())

                    listFollower.clear()
                    for (i in 0 until usersSearch.length()) {
                        val jsonObject = usersSearch.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        val image = jsonObject.getString("avatar_url")
                        val userFollowNew = UserFollow(name, image)
                        listFollower.add(userFollowNew)
                        Log.e("listFollower: ", listFollower.toString())
                        adapterReady()
                    }
                }

                override fun onError(error: ANError) {
                    progressBar.visibility = View.GONE
                    Log.e("onError: ", error.toString())
                }
            })
    }

    private fun adapterReady() {
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        val adapterUser = ItemUserFollowAdapter(listFollower)
        adapterUser.notifyDataSetChanged()
        rvSearchResult.adapter = adapterUser
        progressBar.visibility = View.GONE

        adapterUser.setOnItemClickCallback(object : ItemUserFollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollow) {
                val moveIntent = Intent(this@SearchActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(moveIntent)
            }
        })
    }
}