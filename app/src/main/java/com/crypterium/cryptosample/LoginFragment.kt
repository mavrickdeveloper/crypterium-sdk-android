package com.crypterium.cryptosample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crypterium.cryptosample.api.RequestManager
import com.crypteriumsdk.common.ui.Screens
import com.crypteriumsdk.module.Crypterium
import com.crypteriumsdk.screens.common.domain.JICommonNetworkErrorResponse
import com.crypteriumsdk.screens.common.domain.JICommonNetworkResponse
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private val servers = listOf("https://develop-backend.features.testessential.net/",
        "https://prod-backend.features.testessential.net/",
        "https://sandbox-crpt-backend.features.testessential.net"
    )

    var requestManager: RequestManager = RequestManager(servers.first())
    var ttl = 1000

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        //default
        etMerchantId.setText("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZGNlM2VlMC02M2E1LTRhMTItOTgzNC1kOGY5M2YwNzEzYWEiLCJleHAiOjE2Mjc0ODI2MDcsImlhdCI6MTU5NTkyNTY1NTEyMX0.R-bUvLZo14AlHOJMirn8XB6FNZpuDAvvHv-UiVsaoOA")
        etCustomerId.setText("22b597cc-2fa1-4878-abbf-a469e18c723b")

        btnLogin.setOnClickListener {
            if (!etToken.text.isNullOrEmpty()) {
                Crypterium.getInstance(requireContext()).setAuthToken(etToken.text.toString(), etRefreshToken.text.toString(), ttl)
                (activity as MainActivity).setNavigation(MainActivity.Navigation.DASHBOARD)
            }
        }

        btnOpenSDKAuth.setOnClickListener {
            context?.let {
                Crypterium.getInstance(it).openScreen(it, Screens.AUTHORIZATION)
            }
        }

        btnSetServer.setOnClickListener {
            if (!etServer.text.toString().isEmpty())
                context?.let {
                    Crypterium.getInstance(it).enableTestMode()
                    Crypterium.getInstance(it).configure(serverDropdouwn.selectedItem.toString())

                    Toast.makeText(
                        it,
                        "Server ${etServer.text} configured!",
                        Toast.LENGTH_LONG
                    ).show()

                    requestManager.reInit(etServer.text.toString())
                }
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            servers
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        serverDropdouwn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                etServer.setText(serverDropdouwn.selectedItem.toString())
            }

        }
        serverDropdouwn.adapter = adapter

        serverDropdouwn.setSelection(0)

        btnGetAuthToken.setOnClickListener {
            requestManager.merchantId = etMerchantId.text.toString()
            val customerId = etCustomerId.text.toString()
            requestManager.reInit(etServer.text.toString())
            requestManager.auth(customerId, JICommonNetworkResponse {
                etToken.setText(it.accessToken)
                etRefreshToken.setText(it.refresh_token)
                ttl = it.expires_in

                Toast.makeText(context, "Auth token refreshed", Toast.LENGTH_LONG).show()
            }, JICommonNetworkErrorResponse {
                Toast.makeText(context, "Failed token refresh", Toast.LENGTH_LONG).show()
            })
        }
    }

    companion object {
        const val SAVE_AUTH_TOKEN = "SAVE_AUTH_TOKEN"
        const val SAVE_REFRESH_TOKEN = "SAVE_REFRESH_TOKEN"
        const val SAVE_CUSTOMER_ID = "SAVE_CUSTOMER_ID"
        const val SAVE_MERCHANT_ID = "SAVE_MERCHANT_ID"
        const val SAVE_SERVER_URL = "SAVE_SERVER_URL"
        const val SAVE_TTL = "SAVE_TTL"
    }
}