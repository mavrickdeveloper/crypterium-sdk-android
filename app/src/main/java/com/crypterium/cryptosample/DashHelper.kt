package com.crypterium.cryptosample

import com.crypteriumsdk.data.datatypes.OperationName
import com.crypteriumsdk.screens.dashboard.domain.dto.OperationItemExternalLib

class DashHelper {

    fun getOperation(): MutableList<OperationItemExternalLib> {
        val cryptoService = ArrayList<OperationItemExternalLib>()
        val fiatService = ArrayList<OperationItemExternalLib>()

        cryptoService.add(
            OperationItemExternalLib(
                R.string.f_topup_confirm_submit,
                R.string.f_dashboard_top_up_wallet_subtitle,
                0,
                R.drawable.ic_plus,
                OperationName.PaymentMobile
            )
        )
        cryptoService.add(
            OperationItemExternalLib(
                R.string.f_dashboard_send,
                R.string.f_dashboard_send_subtitle,
                0,
                R.drawable.ic_send,
                OperationName.TransferCrypto
            )
        )
        cryptoService.add(
            OperationItemExternalLib(
                R.string.exchange,
                R.string.f_dashboard_exchange_subtitle,
                R.string.f_select_topup_buy_crypto_best_rates,
                R.drawable.ic_exchange,
                OperationName.ExchangeCrypto
            )
        )


        fiatService.add(
            OperationItemExternalLib(
                R.string.f_dashboard_withdraw,
                R.string.f_dashboard_cash_out_subtitle,
                R.string.f_select_cashout_to_card_world_wide,
                R.drawable.ic_arrow_up,
                OperationName.PayoutCard
            )
        )

        fiatService.add(
            OperationItemExternalLib(
                R.string.home_title_buy_crypto,
                R.string.f_dashboard_buy_crypto_subtitle,
                R.string.f_select_cashout_to_card_low_fees,
                R.drawable.ic_buy_btc,
                OperationName.BuyCryptoByCard
            )
        )
        var operationsExternalLib = mutableListOf<OperationItemExternalLib>()
        operationsExternalLib.add(OperationItemExternalLib(R.string.srypto_service))
        operationsExternalLib.addAll(cryptoService)
        operationsExternalLib.add(OperationItemExternalLib(R.string.srypto_fiat_service))

        operationsExternalLib.addAll(fiatService)

        return operationsExternalLib
    }

}