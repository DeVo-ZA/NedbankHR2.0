package com.company.nedbankhr20.service

import com.sap.cloud.mobile.foundation.model.AppConfig
import com.sap.cloud.android.odata.espmcontainer.ESPMContainer
import com.sap.cloud.mobile.foundation.common.ClientProvider
import com.sap.cloud.mobile.odata.OnlineODataProvider
import com.sap.cloud.mobile.odata.http.OKHttpHandler

class SAPServiceManager(private val appConfig: AppConfig) {

    var serviceRoot: String = ""
        private set
        get() {
            return (eSPMContainer?.provider as OnlineODataProvider).serviceRoot
        }

    var eSPMContainer: ESPMContainer? = null
        private set
        get() {
            return field ?: throw IllegalStateException("SAPServiceManager was not initialized")
        }

    fun openODataStore(callback: () -> Unit) {
        if( appConfig != null ) {
            appConfig.serviceUrl?.let { _serviceURL ->
                eSPMContainer = ESPMContainer (
                    OnlineODataProvider("SAPService", _serviceURL + CONNECTION_ID_ESPMCONTAINER).apply {
                        networkOptions.httpHandler = OKHttpHandler(ClientProvider.get())
                        serviceOptions.checkVersion = false
                        serviceOptions.requiresType = true
                        serviceOptions.cacheMetadata = false
                    }
                )
            } ?: run {
                throw IllegalStateException("ServiceURL of Configuration Data is not initialized")
            }
        }
        callback.invoke()
    }

    companion object {
        const val CONNECTION_ID_ESPMCONTAINER: String = "com.sap.edm.sampleservice.v2"
    }
}
