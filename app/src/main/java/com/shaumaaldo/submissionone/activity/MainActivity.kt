package com.shaumaaldo.submissionone.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.adapter.ItemCardAdapter
import com.shaumaaldo.submissionone.getJsonDataFromAsset
import com.shaumaaldo.submissionone.model.Item
import org.json.JSONObject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    private lateinit var rvItem: RecyclerView
    private lateinit var ibSearch: ImageButton
    private lateinit var tbMain: Toolbar
    private lateinit var obj: JSONObject
    private var userGithubs = ArrayList<Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvItem = findViewById(R.id.rv_item_list)
        tbMain = findViewById(R.id.tb_main)
        ibSearch = findViewById(R.id.ib_search)
        title = "Github"

        setSupportActionBar(tbMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val jsonFileString = getJsonDataFromAsset(applicationContext, "githubuser.json")

        val gson = Gson()
        obj = JSONObject(jsonFileString)
        val data = obj.getString("users")
        val listUserGithubType = object : TypeToken<ArrayList<Item>>() {}.type
        userGithubs = gson.fromJson(data, listUserGithubType)
        Log.e("userGithubs: ", userGithubs.toString())
        userGithubs.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }
        showRecyclerList()

        ibSearch.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }

    private fun showRecyclerList() {
        rvItem.layoutManager = LinearLayoutManager(this)
        val listItemView = ItemCardAdapter(userGithubs)
        rvItem.adapter = listItemView

        listItemView.setOnItemClickCallback(object : ItemCardAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                Toast.makeText(this@MainActivity, data.name, Toast.LENGTH_SHORT).show()
                showSelectedItem(data)
            }
        })
    }

    private fun showSelectedItem(item: Item) {
        Toast.makeText(this, "Kamu memilih " + item.name, Toast.LENGTH_SHORT).show()
        val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_ITEM, item)
        startActivity(moveIntent)
    }
}