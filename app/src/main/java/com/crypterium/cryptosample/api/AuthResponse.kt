package com.crypterium.cryptosample.api

import com.google.gson.annotations.SerializedName


class AuthResponse {
    @SerializedName("access_token")
    var accessToken: String = ""
    @SerializedName("expires_in")
    var expires_in: Int = 0
    @SerializedName("refresh_token")
    var refresh_token: String = ""
}