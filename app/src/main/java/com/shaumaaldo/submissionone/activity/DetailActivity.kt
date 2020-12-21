package com.shaumaaldo.submissionone.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.shaumaaldo.submissionone.BuildConfig
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.adapter.SectionPagerAdapter
import com.shaumaaldo.submissionone.model.UserFollow
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {
    private lateinit var tvUsername: TextView
    private lateinit var tvName: TextView
    private lateinit var tvCompany: TextView
    private lateinit var tvLocation: TextView
    private lateinit var ivImageUserGithub: ImageView
    private lateinit var tvRepo: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tbDetail: Toolbar
    private lateinit var ibShare: ImageButton
    private lateinit var tlFollow: TabLayout
    private lateinit var vpDetail: ViewPager
    private lateinit var rlLoading: RelativeLayout
    private lateinit var fabFavourite: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        AndroidNetworking.initialize(applicationContext)

        tvUsername = findViewById(R.id.tv_username_github)
        tvName = findViewById(R.id.tv_name_github)
        tvCompany = findViewById(R.id.tv_company)
        tvLocation = findViewById(R.id.tv_location)
        ivImageUserGithub = findViewById(R.id.iv_image_user)
        tvRepo = findViewById(R.id.tv_repo)
        tvFollowing = findViewById(R.id.tv_following)
        tvFollowers = findViewById(R.id.tv_followers)
        tbDetail = findViewById(R.id.tb_detail)
        ibShare = findViewById(R.id.ib_share)
        tlFollow = findViewById(R.id.tl_follow)
        vpDetail = findViewById(R.id.vp_detail)
        rlLoading = findViewById(R.id.rl_loading)
        fabFavourite = findViewById(R.id.fab_fav)

        setSupportActionBar(tbDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<UserFollow>(EXTRA_USER)
        val user = item?.nameLogin
        title = item?.nameLogin

        getDetailUser(user)

        ibShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share to")
            intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${item?.nameLogin}")
            startActivity(Intent.createChooser(intent, "choose one"))
        }

        fabFavourite.setOnClickListener {
            Toast.makeText(this.applicationContext, "FAB", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        const val EXTRA_ITEM = "extra_item"
        const val EXTRA_USER = "user_follow"
    }

    private fun getDetailUser(username: String?) {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        vpDetail.adapter = sectionsPagerAdapter
        tlFollow.setupWithViewPager(vpDetail)
        supportActionBar?.elevation = 0f

        AndroidNetworking.get("https://api.github.com/users/{username}")
            .addPathParameter("username", username)
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val login = response?.getString("login")
                        val name = response?.getString("name")
                        val avatar = response?.getString("avatar_url")
                        val company = response?.getString("company")
                        val location = response?.getString("location")
                        val followers = response?.getString("followers")
                        val following = response?.getString("following")
                        val repos = response?.getString("public_repos")

                        tvUsername.text = login.toString()
                        tvName.text = name.toString()
                        tvCompany.text = company.toString()
                        tvLocation.text = location.toString()
                        tvRepo.text = repos.toString()
                        tvFollowing.text = following.toString()
                        tvFollowers.text = followers.toString()

                        Glide.with(ivImageUserGithub)
                            .load(avatar)
                            .error(R.drawable.imagenotavail)
                            .placeholder(R.drawable.pulse_load_image)
                            .circleCrop()
                            .apply(RequestOptions().override(350, 550))
                            .into(ivImageUserGithub)
                        rlLoading.visibility = View.GONE
                    } catch (e: Exception) {
                        rlLoading.visibility = View.GONE
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError?) {
                    rlLoading.visibility = View.GONE
                    Log.e("onErrorDetail: ", anError.toString())
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}