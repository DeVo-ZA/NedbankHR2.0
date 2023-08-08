package com.company.nedbankhr20.app

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.company.nedbankhr20.R



import com.company.nedbankhr20.mdui.EntitySetListActivity

class MainBusinessActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_business)
    }


    private fun startEntitySetListActivity() {
        val sapServiceManager = (application as SAPWizardApplication).sapServiceManager
        sapServiceManager?.openODataStore {
            val intent = Intent(this, EntitySetListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        startEntitySetListActivity()
    }

    companion object {
    }
}
