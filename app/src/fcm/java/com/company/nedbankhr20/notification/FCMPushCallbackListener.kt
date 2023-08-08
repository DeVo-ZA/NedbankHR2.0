package com.company.nedbankhr20.notification

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.company.nedbankhr20.R
import com.company.nedbankhr20.mdui.EntitySetListActivity
import com.sap.cloud.mobile.flowv2.core.DialogHelper
import com.sap.cloud.mobile.foundation.authentication.AppLifecycleCallbackHandler
import com.sap.cloud.mobile.foundation.remotenotification.*

class FCMPushCallbackListener : PushCallbackListener {
    override fun onReceive(context: Context, message: PushRemoteMessage) {

        AppLifecycleCallbackHandler.getInstance().activity?.let { foregroundActivity ->
            showMessageDialog(foregroundActivity as FragmentActivity, message)
        }
    }

    private fun showMessageDialog(activity: FragmentActivity, message: PushRemoteMessage) {
        var textMessage = message.alert ?: activity.getString(R.string.push_text)
        var notificationTitle = message.title?: activity.getString(R.string.push_message)

//        when(notificationTitle){
//            "Personal" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/Av3mNRHBo8plbdCHIZXNrl/overview_page/B6DlwjwRWAPnbEO8QaXKVB?headless=true")
//            "People" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/yGB9snVDCxm5seg2YukgG3/overview_page/LGLxADubYxPjiFbAVgC44V?headless=true")
//            "Company" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/BFnVhucSofCp13e0mnBeVS/overview_page/yzPR0YFVXL5IVBxpdvXWHF?headless=true")
//            "Workspaces" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/member/favorite_groups")
//            else -> {(activity as EntitySetListActivity).changeUrl("https://sfcpart000517.coresalesdemo2.workzone.ondemand.com/")}
//        }
        DialogHelper(activity).showDialogWithCancelAction(
                fragmentManager = activity.supportFragmentManager,
                message = textMessage,
                negativeAction = {},
                title = notificationTitle,
                positiveAction = {
                    message.notificationID?.let {
                        BasePushService.setPushMessageStatus(it, PushRemoteMessage.NotificationStatus.CONSUMED)
                        when(notificationTitle){
                            "Personal" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/Av3mNRHBo8plbdCHIZXNrl/overview_page/B6DlwjwRWAPnbEO8QaXKVB?headless=true")
                            "People" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/yGB9snVDCxm5seg2YukgG3/overview_page/LGLxADubYxPjiFbAVgC44V?headless=true")
                            "Company" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/groups/BFnVhucSofCp13e0mnBeVS/overview_page/yzPR0YFVXL5IVBxpdvXWHF?headless=true")
                            "Workspaces" -> (activity as EntitySetListActivity).changeUrl("https://eua-wz-sub-ys5hj8ga.workzonehr.cfapps.eu10.hana.ondemand.com/site#workzone-home&/member/favorite_groups")
                            else -> {(activity as EntitySetListActivity).changeUrl("https://sfcpart000517.coresalesdemo2.workzone.ondemand.com/")}
                        }
                    }
                }
        )
    }

}
