package com.ssindher.truecallerauth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.truecaller.android.sdk.TrueProfile
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val trueProfile = intent.getParcelableExtra<TrueProfile>("profile")
        if (trueProfile != null) {
            setupUI(trueProfile)
        } else {
            Toast.makeText(this, "TrueProfile is null", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(trueProfile: TrueProfile) {
        textViewWelcome.text = "Welcome ${trueProfile.firstName} ${trueProfile.lastName}"
        textViewPhoneNumber.text = trueProfile.phoneNumber
        textViewEmail.text = trueProfile.email
    }

}