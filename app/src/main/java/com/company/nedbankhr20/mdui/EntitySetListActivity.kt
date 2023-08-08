package com.company.nedbankhr20.mdui

import com.company.nedbankhr20.app.SAPWizardApplication

import com.sap.cloud.mobile.flowv2.core.DialogHelper
import com.sap.cloud.mobile.flowv2.core.Flow
import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry
import com.sap.cloud.mobile.flowv2.model.FlowType
import com.sap.cloud.mobile.flowv2.securestore.UserSecureStoreDelegate
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*
import android.widget.ArrayAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.ArrayList
import java.util.HashMap
import com.company.nedbankhr20.app.WelcomeActivity
import com.company.nedbankhr20.R

import kotlinx.android.synthetic.main.activity_entity_set_list.*
import kotlinx.android.synthetic.main.element_entity_set_list.view.*
import org.slf4j.LoggerFactory

/*
 * An activity to display the list of all entity types from the OData service
 */
class EntitySetListActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //navigate to launch screen if SAPServiceManager or OfflineOdataProvider is not initialized
        navForInitialize()
        setContentView(R.layout.activity_home_page)

        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true;
        webView.settings.builtInZoomControls = true;
        webView.settings.displayZoomControls = false;
        webView.settings.domStorageEnabled = true;
        //webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        Log.v("UA", webView.settings.userAgentString);
        val url = "https://sfcpart000517.coresalesdemo2.workzone.ondemand.com/"
        webView.webViewClient = WebViewClient()

        webView.loadUrl(url);
    }

    fun changeUrl(url: String){
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun navForInitialize() {
        if ((application as SAPWizardApplication).sapServiceManager == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


    companion object {
        private val LOGGER = LoggerFactory.getLogger(EntitySetListActivity::class.java)
    }
}
