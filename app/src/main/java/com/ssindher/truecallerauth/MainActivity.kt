package com.ssindher.truecallerauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.truecaller.android.sdk.*
import com.truecaller.android.sdk.clients.VerificationCallback
import com.truecaller.android.sdk.clients.VerificationDataBundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var trueScope: TruecallerSdkScope
    private lateinit var sdkCallback: ITrueCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTruecaller()

        buttonLogin.setOnClickListener { TruecallerSDK.getInstance().getUserProfile(this) }
    }

    private fun setupTruecaller() {
        sdkCallback = object : ITrueCallback {
            override fun onSuccessProfileShared(trueProfile: TrueProfile) {
                Toast.makeText(this@MainActivity, "onSuccessProfileShared", Toast.LENGTH_SHORT)
                    .show()
                gotoHome(trueProfile)
            }

            override fun onFailureProfileShared(p0: TrueError) {
                Toast.makeText(
                    this@MainActivity,
                    "onFailureProfileShared: ${p0.errorType}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onVerificationRequired() {
                gotoVerification()
            }

        }
        trueScope = TruecallerSdkScope.Builder(this, sdkCallback)
            .consentMode(TruecallerSdkScope.CONSENT_MODE_POPUP)
            .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_VERIFY)
            .footerType(TruecallerSdkScope.FOOTER_TYPE_SKIP)
            .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITH_OTP)
            .build()
        TruecallerSDK.init(trueScope)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TruecallerSDK.getInstance().onActivityResultObtained(this, resultCode, data)
    }

    private fun gotoHome(trueProfile: TrueProfile) {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("profile", trueProfile)
        startActivity(intent)
    }

    private fun gotoVerification() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}