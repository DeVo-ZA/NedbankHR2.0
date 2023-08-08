package com.company.nedbankhr20.mdui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.preference.*

import com.company.nedbankhr20.R
import com.company.nedbankhr20.app.WelcomeActivity
import com.sap.cloud.mobile.flowv2.model.FlowType
import com.sap.cloud.mobile.flowv2.core.Flow.Companion.start
import com.sap.cloud.mobile.flowv2.model.FlowConstants
import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry.flowContext

import android.util.Log
import com.company.nedbankhr20.notification.PushService
import com.sap.cloud.mobile.foundation.mobileservices.SDKInitializer
import com.sap.cloud.mobile.foundation.mobileservices.ServiceResult
import com.sap.cloud.mobile.foundation.remotenotification.RemoteNotificationClient


/** This fragment represents the settings screen. */
class SettingsFragment : PreferenceFragmentCompat() {
    private var changePassCodePreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        changePassCodePreference = findPreference(getString(R.string.manage_passcode))
        changePassCodePreference!!.setOnPreferenceClickListener {
            changePassCodePreference!!.isEnabled = false
            val flowContext =
                flowContext.copy(flowType = FlowType.CHANGEPASSCODE)
            start(requireActivity(), flowContext) { requestCode, _, _ ->
                if (requestCode == FlowConstants.FLOW_ACTIVITY_REQUEST_CODE) {
                    changePassCodePreference!!.isEnabled = true
                }
            }
            false
        }
        // Reset App
        val resetAppPreference : Preference = findPreference(getString(R.string.reset_app))!!
        resetAppPreference.setOnPreferenceClickListener {
            start(
                activity = requireActivity(),
                flowContext = flowContext.copy(flowType = FlowType.RESET),
                flowActivityResultCallback = { _, resultCode, _ ->
                    if (resultCode == Activity.RESULT_OK) {
                        Intent(requireActivity(), WelcomeActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(this)
                        }
                    }
                })
            false
        }

        // subscribe push topics
        // mock topics list retrieved from server
        val topicKeys = arrayOf("topic1", "topic2", "topic3")
        topicKeys.forEach {
            try {
                requireActivity().runOnUiThread {
                    var tPushTopicPreference: SwitchPreference = findPreference(it)!!
                    tPushTopicPreference.isChecked = false
                }
            } catch(e: Throwable) {
                Log.e("UpdatePushTopicsStateFailed", "###Error: $e", e)
            }
        }
        preparePushTopic(topicKeys)
    }

    override fun onResume() {
        super.onResume()
    }
    private fun preparePushTopic(topicKeys: Array<String>) {
        val pushService: PushService = PushService()
        SDKInitializer.getService(pushService.getPushService()::class)?.getAllSubscribedTopics(object: RemoteNotificationClient.CallbackListenerWithResult {
            override fun onSuccess(result: ServiceResult.SUCCESS<Array<String?>>) {
                result.data?.forEach {
                    try {
                        requireActivity().runOnUiThread {
                            var pushTopicPreference: SwitchPreference = it?.let { it1 ->
                                findPreference(it1)
                            }!!
                            pushTopicPreference.isChecked = true
                        }
                    } catch(e: Throwable) {
                        Log.e("GetAllPushTopicsFailed", "###Error: $e", e)
                    }
                }
            }
            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }
        })

        topicKeys.forEach {
            var oPushTopicPreference: SwitchPreference = findPreference(it)!!
            oPushTopicPreference.setOnPreferenceChangeListener { preference, newValue ->
                if (preference is SwitchPreference) {
                    if (newValue == true) {
                        SDKInitializer.getService(pushService.getPushService()::class)?.subscribeTopic(preference.key, object : RemoteNotificationClient.CallbackListener {
                            override fun onSuccess() {
                                requireActivity().runOnUiThread {
                                    preference.isChecked = true
                                }
                            }
                            override fun onError(e: Throwable) {
                                Log.e("SubscribeTopicFailed", "###Error: $e", e)
                            }
                        })
                    } else {
                        SDKInitializer.getService(pushService.getPushService()::class)?.unsubscribeTopic(preference.key, object : RemoteNotificationClient.CallbackListener {
                            override fun onSuccess() {
                                requireActivity().runOnUiThread {
                                    preference.isChecked = false
                                }
                            }
                            override fun onError(e: Throwable) {
                                Log.e("UnsubscribeTopicFailed", "###Error: $e", e)
                            }
                        })
                    }
                }
                false
            }
        }
    }
}
