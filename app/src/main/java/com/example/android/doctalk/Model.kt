package com.example.android.doctalk

/**
 * Created by Android on 3/26/2018.
 */
data class Model(var items: ArrayList<Item>) {

    data class Item(var login: String,
                    var id: Int, var avatar_url: String?,
                    var url: String)

}