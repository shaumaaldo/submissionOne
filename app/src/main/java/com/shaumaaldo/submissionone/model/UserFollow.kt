package com.shaumaaldo.submissionone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFollow(
    val nameLogin: String,
    val image: String
    ) : Parcelable



