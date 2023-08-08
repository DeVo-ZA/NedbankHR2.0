package com.company.nedbankhr20.app

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.sap.cloud.mobile.foundation.model.AppConfig
import com.company.nedbankhr20.service.SAPServiceManager
import com.company.nedbankhr20.repository.RepositoryFactory
import com.sap.cloud.mobile.foundation.mobileservices.MobileService
import com.sap.cloud.mobile.foundation.mobileservices.SDKInitializer
import com.company.nedbankhr20.notification.PushService
import com.sap.cloud.mobile.foundation.remotenotification.*
import android.content.res.Configuration
import com.sap.cloud.mobile.foundation.theme.ThemeDownloadService


class SAPWizardApplication: Application() {

    internal var isApplicationUnlocked = false
    lateinit var preferenceManager: SharedPreferences

    /**
     * Manages and provides access to OData stores providing data for the app.
     */
    internal var sapServiceManager: SAPServiceManager? = null
    /**
     * Application-wide RepositoryFactory
     */
    lateinit var repositoryFactory: RepositoryFactory
        private set

    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        initServices()
    }

    /**
     * Initialize service manager with application configuration
     *
     * @param appConfig the application configuration
     */
    fun initializeServiceManager(appConfig: AppConfig) {
        sapServiceManager = SAPServiceManager(appConfig)
        repositoryFactory =
            RepositoryFactory(sapServiceManager)
    }

    /**
     * Clears all user-specific data and configuration from the application, essentially resetting it to its initial
     * state.
     *
     * If client code wants to handle the reset logic of a service, here is an example:
     *
     *   SDKInitializer.resetServices { service ->
     *       return@resetServices if( service is PushService ) {
     *           PushService.unregisterPushSync(object: CallbackListener {
     *               override fun onSuccess() {
     *               }
     *
     *               override fun onError(p0: Throwable) {
     *               }
     *           })
     *           true
     *       } else {
     *           false
     *       }
     *   }
     */
    fun resetApplication() {
        preferenceManager.also {
            it.edit().clear().apply()
        }
        isApplicationUnlocked = false
        repositoryFactory.reset()
        SDKInitializer.resetServices()

    }

    private fun initServices() {
        val services = mutableListOf<MobileService>()
        val parameters = RemoteNotificationParameters.Builder()
            .userLocale(resources.configuration.locale.toString())
            .build()

        var pushService: PushService = PushService()
        services.add(pushService.getInitializedPushService(parameters, this@SAPWizardApplication, packageManager, packageName))
        services.add(ThemeDownloadService(this))

        SDKInitializer.start(this, * services.toTypedArray())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val parameters = RemoteNotificationParameters.Builder()
            .userLocale(newConfig.locale.toString())
            .build()
		var pushService: PushService = PushService()
        SDKInitializer.getService(pushService.getPushService()::class)?.updateRegistration(
            parameters,
            object : RemoteNotificationClient.CallbackListener {
                override fun onSuccess() {
                    //TO-DO
                }

                override fun onError(e: Throwable) {
                    //TO-DO
                }
            })
    }

}