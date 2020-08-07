package com.crypterium.cryptosample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.crypteriumsdk.common.locale.LocaleAwareCompatActivity
import com.crypteriumsdk.module.Crypterium
import com.crypteriumsdk.module.CrypteriumInterface

class MainActivity : LocaleAwareCompatActivity() {

    private var navigation: Navigation = Navigation.LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Crypterium.appCallback = object : CrypteriumInterface {
            override fun onFinish() {
                navigation = Navigation.LOGIN
            }

            override fun onTokenExpired() {
                this@MainActivity.runOnUiThread {
                    Toast.makeText(this@MainActivity, "Token expired", Toast.LENGTH_LONG).show()
                }
                navigation = Navigation.LOGIN

                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this@MainActivity.startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        navigation = (if (Crypterium.getInstance(this.applicationContext).isLoggedIn()) Navigation.DASHBOARD else Navigation.LOGIN)
        performNavigation()
    }

    fun setNavigation(navigation: Navigation) {
        this.navigation = navigation
        performNavigation()
    }

    private fun performNavigation() {
        if (isDestroyed) return

        when(navigation) {
            Navigation.LOGIN -> openLogin()
            Navigation.DASHBOARD -> openDashboard()
        }
    }

    private fun openDashboard() {
        if (supportFragmentManager.fragments.getOrNull(0) !is DashboardFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, DashboardFragment())
                .commit()
        }
    }

    private fun openLogin() {
        if (supportFragmentManager.fragments.getOrNull(0) !is LoginFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, LoginFragment())
                .commit()
        }
    }

    enum class Navigation() {
        LOGIN,
        DASHBOARD
    }
}
