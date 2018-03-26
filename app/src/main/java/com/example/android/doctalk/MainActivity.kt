package com.example.android.doctalk

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.doctalk.databinding.ActivityMainBinding
import com.example.android.doctalk.databinding.RowLayoutBinding
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v7.widget.DividerItemDecoration



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        val list = arrayListOf<Model.Item>()
        var items = arrayListOf<Model.Item>()
        var gitResponseRetrofit: Call<Model>? = null
        recycler_view.layoutManager = LinearLayoutManager(this)
//        globally declared to maintain cancel call
        var lastAdapter: LastAdapter? = null
        val client = ServiceGenerator.getClient()?.create(RestAPI::class.java)
        val dividerItemDecoration = DividerItemDecoration(recycler_view.context,
                (recycler_view.layoutManager as LinearLayoutManager).orientation)
        recycler_view.addItemDecoration(dividerItemDecoration)
        edit_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gitResponseRetrofit?.cancel()
//                the call is cancelled because of previous calls getting piled up on text changed
                if (p0?.length == 0) {
//                    if there is no item in list then the recycler_view will be empty
                    items.clear()
                    lastAdapter?.notifyDataSetChanged()
                } else {
                    gitResponseRetrofit = client?.gitResponse(p0.toString(), "followers")!!

                    gitResponseRetrofit?.enqueue(object : Callback<Model?> {
                        override fun onFailure(call: Call<Model?>?, t: Throwable?) {
//                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<Model?>?, response: Response<Model?>?) {

                            if (response != null) {
                                if (response.isSuccessful) {
                                    Log.d("TAG", "response code" + response.code())
                                    items = response.body()?.items!!
//                                    Last Adapter Library
                                    lastAdapter = LastAdapter(items, BR.item).map<Model.Item, RowLayoutBinding>(R.layout.row_layout) {
                                        onBind {
                                            it.binding.name.text = items[it.adapterPosition].login
                                            it.binding.followers.text = items[it.adapterPosition].url
                                            Glide.with(it.binding.image).load(items[it.adapterPosition].avatar_url)
                                                    .apply(RequestOptions().circleCrop().centerCrop()
                                                            .error(R.drawable.abc_ic_ab_back_material)).into(it.binding.image)
                                        }
                                    }.into(recycler_view)
                                }
                            }

                        }
                    })
                }
            }
        })
    }

}
