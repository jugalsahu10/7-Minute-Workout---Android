package com.android.a7minuteworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.a7minuteworkout.databinding.ActivityBmiBinding
import java.text.DecimalFormat

class BMIActivity : AppCompatActivity() {

    var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbBmi)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.tbBmi?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnCalculateBMI?.setOnClickListener {
            if (validateValues()) {

                val bmi: Double
                if (binding?.rbMetrics?.isChecked!!) {
                    val weight = binding?.etMetricUnitWeight?.text.toString().toDouble()
                    val height = binding?.etMetricUnitHeight?.text.toString().toDouble() / 100
                    bmi = roundTo2Decimal(weight / height / height)
                } else {
                    val weight = binding?.etUSUnitWeight?.text.toString().toDouble()
                    val height = 12 * binding?.etUSUnitFeet?.text.toString()
                        .toDouble() + binding?.etUSUnitInch?.text.toString().toDouble()
                    bmi = roundTo2Decimal(703 * (weight / height / height))
                }

                displayBMIResult(bmi)
            } else {
                Toast.makeText(
                    this@BMIActivity, "Please enter weight and height", Toast.LENGTH_LONG
                ).show()
            }
        }

        binding?.rbMetrics?.setOnCheckedChangeListener { buttonView, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            binding?.tilMetricUnitHeight?.visibility = visibility
            binding?.tilMetricUnitWeight?.visibility = visibility
            clearBMIFields()
        }

        binding?.rbUS?.setOnCheckedChangeListener { buttonView, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            binding?.tilUSUnitWeight?.visibility = visibility
            binding?.llUSUnitHeight?.visibility = visibility
            clearBMIFields()
        }
    }

    private fun clearBMIFields() {
        binding?.etMetricUnitWeight?.text?.clear()
        binding?.etMetricUnitHeight?.text?.clear()
        binding?.etUSUnitWeight?.text?.clear()
        binding?.etUSUnitFeet?.text?.clear()
        binding?.etUSUnitInch?.text?.clear()
    }

    private fun roundTo2Decimal(num: Double): Double {
        val df = DecimalFormat("#.##")
        return df.format(num).toDouble()
    }

    private fun displayBMIResult(bmi: Double) {
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmi.toString()
    }

    private fun validateValues(): Boolean {
        return if (binding?.rbMetrics?.isChecked!!) {
            binding?.etMetricUnitWeight?.text.toString()
                .isNotEmpty() && binding?.etMetricUnitHeight?.text.toString().isNotEmpty()
        } else {
            binding?.etUSUnitWeight?.text.toString()
                .isNotEmpty() && binding?.etUSUnitFeet?.text.toString()
                .isNotEmpty() && binding?.etUSUnitInch?.text.toString().isNotEmpty()
        }
    }
}