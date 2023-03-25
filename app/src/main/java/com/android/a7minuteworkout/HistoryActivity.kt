package com.android.a7minuteworkout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a7minuteworkout.adapter.HistoryAdapter
import com.android.a7minuteworkout.databinding.ActivityHistoryBinding
import com.android.roomdemo.HistoryEntity
import com.android.roomdemo.HistoryViewModel
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null
    private var historyViewModel: HistoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbHistory)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.tbHistory?.setNavigationOnClickListener {
            onBackPressed()
        }

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        lifecycleScope.launch {
            historyViewModel?.fetchAllHistory()?.collect {
                setExerciseHistoryList(ArrayList(it))
            }
        }
    }

    private fun setExerciseHistoryList(exerciseHistoryList: ArrayList<HistoryEntity>) {
        if (exerciseHistoryList.isNotEmpty()) {
            val historyAdapter = HistoryAdapter(exerciseHistoryList)

            binding?.tvRecords?.layoutManager = LinearLayoutManager(this)
            binding?.tvRecords?.adapter = historyAdapter

            binding?.tvNoRecords?.visibility = View.INVISIBLE
            binding?.tvRecords?.visibility = View.VISIBLE
        } else {
            binding?.tvNoRecords?.visibility = View.VISIBLE
            binding?.tvRecords?.visibility = View.INVISIBLE
        }
    }
}