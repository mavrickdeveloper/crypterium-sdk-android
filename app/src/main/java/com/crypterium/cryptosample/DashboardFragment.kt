package com.crypterium.cryptosample

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crypteriumsdk.common.ui.Screens
import com.crypteriumsdk.data.datatypes.OperationName
import com.crypteriumsdk.module.Crypterium
import com.crypteriumsdk.module.CrypteriumInterface
import com.crypteriumsdk.screens.common.domain.dto.*
import com.crypteriumsdk.screens.common.domain.utils.FormatterHelper
import com.crypteriumsdk.screens.common.domain.utils.ResourceHelper
import com.crypteriumsdk.screens.dashboard.domain.dto.OperationItemExternalLib
import com.crypteriumsdk.screens.dashboard.domain.dto.WalletItem
import com.crypteriumsdk.screens.dashboard.presentation.OperationsAdapterExternalLib
import com.crypteriumsdk.screens.dashboard.presentation.WalletsAdapter
import kotlinx.android.synthetic.main.fragment_dash.*
import java.math.BigDecimal

class DashboardFragment : Fragment() {
    private lateinit var walletsList: MutableList<WalletItem>
    private lateinit var walletsResponse: WalletResponse

    var isWalletsVisible = false

    private var bottomWalletsSwitcherScrollPosition: Byte = 0
    private var showBottomWalletsSwitcherVisibility: Boolean = false
    private var changeTypeIsPercantage: Boolean = false
    private var walletsAdapter: WalletsAdapter? = null

    private var scrollState: IntArray? = null

    private var initialImagePadding = 0f

    private var operationsAdapter: OperationsAdapterExternalLib? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollState = savedInstanceState?.getIntArray(ARG_SCROLL_POSITION)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.fragment_dash, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        getWalletItems()
        setupWallets()
    }

    private fun setupView() {
        showLoader()

        operationsAdapter = OperationsAdapterExternalLib(
            context,
            callback = object : OperationsAdapterExternalLib.ClickListener {
                override fun onItemClicked(operation: OperationItemExternalLib?) {
                    onOperationClicked(operation)
                }

            })

        rvOperations.setHasFixedSize(true)
        rvOperations.layoutManager = LinearLayoutManager(context)
        rvOperations.adapter = operationsAdapter

        initialImagePadding = ResourceHelper.dpToPx(context, 50f)

        if (context != null) {
            val loaderColor = ContextCompat.getColor(context!!, R.color.blueterium_100)
            refreshLayout.setColorSchemeColors(loaderColor, loaderColor, loaderColor, loaderColor)
        }
        refreshLayout.setOnRefreshListener {
            getWalletItems()
        }


        flHistory.setOnClickListener { openHistory() }
        flProfile.setOnClickListener { openProfile() }

        setupWallets()

        tvWalletsSwitcher.setOnClickListener { if (isWalletsVisible) hideWallets() else showWallets() }
        tvChangeMain.setOnClickListener { swapChange() }
        tvChangeTitle.setOnClickListener { swapChange() }

        scroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            checkScrollState()
        }

    }

    private fun openProfile() {
        openScreen(Screens.PROFILE)
    }

    private fun openHistory() {
        openScreen(Screens.HISTORY)
    }

    private fun onOperationClicked(operation: OperationItemExternalLib?) {
        startOperation(operation?.operationName, operation?.provider)
    }

    private fun startOperation(operationName: OperationName?, provider: String?) {
        when (operationName) {
            OperationName.ExchangeCrypto -> openScreen(Screens.EXCHANGE)
            OperationName.TransferCrypto -> openScreen(Screens.SEND_BY_WALLET)
            OperationName.PaymentMobile -> openScreen(Screens.RECEIVE)
            OperationName.PayoutCard -> openScreen(Screens.PAYOUT)
            OperationName.BuyCryptoByCard -> openScreen(Screens.PAYIN)
        }
    }

    private fun openScreen(screen: Screens) {
        Crypterium.getInstance(requireContext()).openScreen(requireContext(), screen)

    }

    private fun checkScrollState() {
        if (showBottomWalletsSwitcherVisibility && scroll.scrollY <= bottomWalletsSwitcherScrollPosition) {
            flWalletsSwitcher2.visibility = View.VISIBLE
        } else {
            flWalletsSwitcher2.visibility = View.GONE
        }
    }


    private fun restoreScrollView() {
        if (scrollState != null && scrollState!!.size == 2)
            scroll.post {
                scroll?.scrollTo(scrollState!![0], scrollState!![1])
            }
    }

    override fun onPause() {
        super.onPause()

        scrollState = intArrayOf(scroll.scrollX, scroll.scrollY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (scroll != null) {
            outState.putIntArray(
                ARG_SCROLL_POSITION,
                intArrayOf(scroll.scrollX, scroll.scrollY)
            )
        }
        super.onSaveInstanceState(outState)
    }

    private fun setupWallets() {
        walletsAdapter =
            WalletsAdapter(context, callback = object : WalletsAdapter.WalletsAdapterListener {
                override fun onWalletClicked(wallet: Wallet?) {
                    //todo open wallet screen
                }
            })
        rvWallets.setHasFixedSize(true)
        rvWallets.layoutManager = LinearLayoutManager(context)
        rvWallets.adapter = walletsAdapter
    }

    private fun getWalletItems() {
        Crypterium.getInstance(requireContext()).CRPTWalletsManager.getResponse {
            if (!isAdded) {
                return@getResponse
            }
            walletsResponse = it
            val walletsItems = mutableListOf<WalletItem>()

            for (wallet in walletsResponse.wallets!!) {
                val balance = FormatterHelper.instance.format(wallet.balance, true)

                val deviationPercent: BigDecimal? = wallet.fiat?.changePercent

                val balanceInPrimary = Price(
                    wallet.fiat?.amount,
                    wallet.fiat?.customerCurrency
                ).formattedPriceWithCurrencyFirst()

                var formattedChange = ""
                var changeTextColorRes = R.color.walletPercentPlus
                if (deviationPercent != null) {
                    formattedChange = deviationPercent.toPlainString() + "%"
                    if (deviationPercent.signum() == 1) {
                        formattedChange = "+$formattedChange"
                    }

                    changeTextColorRes =
                        when {
                            deviationPercent.signum() == 1 -> R.color.walletPercentPlus
                            deviationPercent.signum() == -1 -> R.color.walletPercentMinus
                            else -> R.color.text_primary_white
                        }
                }

                walletsItems.add(
                    WalletItem(
                        wallet,
                        wallet.currency,
                        balance,
                        balanceInPrimary,
                        formattedChange,
                        changeTextColorRes,
                        CryptoCurrencyType.getCryptoCurrencyIcon(wallet.currency)
                    )
                )
            }

            walletsList = walletsItems
            updateWallets()
            hideLoader()
        }
    }

    fun updateWallets() {
        //todo
        operationsAdapter?.updateItems(DashHelper().getOperation())

        walletsAdapter?.updateWalletItems(walletsList)

        tvTotalBalance?.text =
            FormatterHelper.instance.format(walletsResponse.fiat?.amount, true)
        tvPrimaryCurrency?.text =
            CurrencyType.getCurrencySign(walletsResponse.fiat?.customerCurrency)

        if (changeTypeIsPercantage!!) {
            tvChangeMain?.text = FormatterHelper.instance.formatWithCurrency(
                walletsResponse.fiat?.changePercent,
                true,
                "%"
            )
        } else {
            tvChangeMain?.text = FormatterHelper.instance.formatWithCurrency(
                walletsResponse.fiat?.change,
                true,
                walletsResponse.fiat?.customerCurrency
            )
        }

        calculateTextWidth()

        checkScrollState()
    }

    private fun calculateTextWidth() {
        val maxTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            28f,
            context?.resources?.displayMetrics
        )

        val maxWidth = tvTotalBalance.width + tvChangeMain.width - TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            context?.resources?.displayMetrics
        )
        val allText = tvChangeMain.text.toString() + tvTotalBalance.text.toString()

        val textPaint = Paint()
        textPaint.set(tvChangeMain.paint)
        textPaint.textSize = maxTextSize
        val currWidth = textPaint.measureText(allText).toInt()

        var targetTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            10f,
            context?.resources?.displayMetrics
        )
        if (currWidth > maxWidth || tvTotalBalance.textSize < maxTextSize) {
            var textSizeTmp = maxTextSize
            val threshold = 1
            while (textSizeTmp - targetTextSize > threshold) {
                val size = (textSizeTmp + targetTextSize) / 2.0f
                textPaint.textSize = size
                if (textPaint.measureText(allText).toInt() >= maxWidth)
                    textSizeTmp = size // too big
                else
                    targetTextSize = size // too small
            }

            tvTotalBalance.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize)
            tvChangeMain.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize)
            var currencySize = targetTextSize / 1.55f
            if (currencySize < 10f)
                currencySize = 10f
            tvPrimaryCurrency.setTextSize(TypedValue.COMPLEX_UNIT_PX, currencySize)
        }
    }

    private fun swapChange() {
        //todo
        if (changeTypeIsPercantage!!) {
            tvChangeMain?.text =
                FormatterHelper.instance.formatWithCurrency(
                    walletsResponse.fiat?.change,
                    true, walletsResponse.fiat?.customerCurrency
                )//summaryChangeNotPercentage
            changeTypeIsPercantage = false
        } else {
            tvChangeMain?.text = FormatterHelper.instance.formatWithCurrency(
                walletsResponse.fiat?.changePercent,
                true, "%"
            ) //summaryChange
            changeTypeIsPercantage = true
        }

        calculateTextWidth()
    }

    private fun hideWallets() {
        tvWalletsSwitcher.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_down_blueterium_small,
            0
        )
        tvWalletsSwitcher.setText(R.string.f_dashboard_wallets_show_text)

        rvWallets.visibility = View.GONE
        isWalletsVisible = false
    }

    private fun showWallets() {
        tvWalletsSwitcher.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_up_blueterium_small,
            0
        )
        tvWalletsSwitcher.setText(R.string.f_dashboard_wallets_hide_text)
        rvWallets.visibility = View.VISIBLE
        isWalletsVisible = true
    }


    private fun showLoader() {
        refreshLayout.setRefreshing(true)
    }

    private fun hideLoader() {
        refreshLayout.setRefreshing(false)
    }

    private fun redirectStore() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url)))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {

        private const val ARG_SCROLL_POSITION = "scrollPosition"

    }
}