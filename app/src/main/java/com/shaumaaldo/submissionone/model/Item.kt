package com.shaumaaldo.submissionone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var username: String?,
    var name: String?,
    var avatar: String?,
    var company: String?,
    var location: String?,
    var repository: Int?,
    var follower: Int?,
    var following: Int?
) : Parcelable