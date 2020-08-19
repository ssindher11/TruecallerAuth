package com.ssindher.truecallerauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.truecaller.android.sdk.TrueException
import com.truecaller.android.sdk.TrueProfile
import com.truecaller.android.sdk.TruecallerSDK
import com.truecaller.android.sdk.clients.VerificationCallback
import com.truecaller.android.sdk.clients.VerificationDataBundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "RegisterActivityTAG"
    }

    private var type = -1 // 1 = Dropcall, 2 = OTP
    private lateinit var profile: TrueProfile

    private val apiCallback = object : VerificationCallback {
        override fun onRequestSuccess(requestCode: Int, extras: VerificationDataBundle?) {
            if (requestCode == VerificationCallback.TYPE_MISSED_CALL_INITIATED) {
                Log.v(TAG, "TYPE_MISSED_CALL_INITIATED")
            }
            if (requestCode == VerificationCallback.TYPE_MISSED_CALL_RECEIVED) {
                Log.v(TAG, "TYPE_MISSED_CALL_RECEIVED")
                textInputLayoutName.visibility = View.VISIBLE
                buttonContinue.visibility = View.VISIBLE
                type = 1
            }
            if (requestCode == VerificationCallback.TYPE_OTP_INITIATED) {
                Log.v(TAG, "TYPE_OTP_INITIATED")
                textInputLayoutOTP.visibility = View.VISIBLE
                type = 2
            }
            if (requestCode == VerificationCallback.TYPE_OTP_RECEIVED) {
                Log.v(TAG, "TYPE_OTP_RECEIVED")
                textInputLayoutName.visibility = View.VISIBLE
                buttonContinue.visibility = View.VISIBLE
            }
            if (requestCode == VerificationCallback.TYPE_VERIFICATION_COMPLETE) {
                Log.v(TAG, "TYPE_VERIFICATION_COMPLETE")
                gotoHome(profile)
            }
            if (requestCode == VerificationCallback.TYPE_PROFILE_VERIFIED_BEFORE) {
                Log.v(TAG, "TYPE_PROFILE_VERIFIED_BEFORE")
            }
        }

        override fun onRequestFailure(p0: Int, p1: TrueException) {
            Log.v(TAG, "exception: ${p1.exceptionMessage}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupListeners()
    }

    private fun setupListeners() {
        buttonVerify.setOnClickListener {
            val number = editTextMobileNumber.text.toString()
            TruecallerSDK.getInstance().requestVerification("IN", number, apiCallback, this)
        }

        buttonContinue.setOnClickListener {
            val name = editTextName.text.toString()
            profile = TrueProfile.Builder(name, " ").build()
            if (type == 1) {
                TruecallerSDK.getInstance().verifyMissedCall(profile, apiCallback)
            } else if (type == 2) {
                val otp = editTextOTP.text.toString()
                TruecallerSDK.getInstance().verifyOtp(profile, otp, apiCallback)
            }
        }
    }

    private fun gotoHome(trueProfile: TrueProfile) {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("profile", trueProfile)
        startActivity(intent)
    }
}