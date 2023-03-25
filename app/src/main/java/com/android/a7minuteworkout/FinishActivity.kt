package com.android.a7minuteworkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.a7minuteworkout.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {

    var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnFinish?.setOnClickListener {
            finish()
            val intent = Intent(this@FinishActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}