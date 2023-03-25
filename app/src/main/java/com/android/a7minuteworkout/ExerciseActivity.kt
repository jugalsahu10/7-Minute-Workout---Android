package com.android.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.a7minuteworkout.databinding.ActivityExerciseBinding
import com.android.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding
import com.android.roomdemo.HistoryEntity
import com.android.roomdemo.HistoryViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var binding: ActivityExerciseBinding? = null
    var restTimer: CountDownTimer? = null
    var restProgress: Int = 0
    var exerciseList: List<ExerciseModel>? = null
    var currentExercisePosition: Int = -1
    var tts: TextToSpeech? = null
    var player: MediaPlayer? = null
    var exerciseStatusAdaptor: ExerciseStatusAdaptor? = null
    private val countDownValue: Long = 10

    private var historyViewModel: HistoryViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        tts = TextToSpeech(this, this)

        setSupportActionBar(binding?.toolbarExercise)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        exerciseList = Constants.defaultExerciseList()

        try {
            val soundUri =
                Uri.parse("android.resource://com.android.a7minuteworkout/" + R.raw.relax)
            player = MediaPlayer.create(applicationContext, soundUri)
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error while setting up media player")
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        setRestProgressBar(true, "GET READY FOR", countDownValue)
        setupExerciseStatusAdapter()

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this@ExerciseActivity)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.create()
        customDialog.show()
    }

    private fun setupExerciseStatusAdapter() {
        exerciseStatusAdaptor = ExerciseStatusAdaptor(exerciseList!!)

        binding?.rvExerciseStatus?.adapter = exerciseStatusAdaptor
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setRestProgressBar(rest: Boolean, countDownName: String, countDownValue: Long) {
        setupRestTimer()
        binding?.tvTitle?.text = countDownName
        binding?.progressBar?.progress = countDownValue.toInt()
        binding?.progressBar?.max = countDownValue.toInt()
        binding?.tvTimer?.text = countDownValue.toString()
        if (!rest) {
            binding?.upcommingTitle?.visibility = View.INVISIBLE
            binding?.upcommingExercise?.visibility = View.INVISIBLE
            binding?.rvExerciseStatus?.visibility = View.VISIBLE
            if (currentExercisePosition >= 0 && currentExercisePosition < exerciseList!!.size) {
                binding?.ivImage?.visibility = View.VISIBLE
                val exerciseImage = exerciseList?.get(currentExercisePosition)!!.image
                binding?.ivImage?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ExerciseActivity,
                        exerciseImage
                    )
                )
            }
            speakText(exerciseList?.get(currentExercisePosition)!!.name)
        } else {
            binding?.ivImage?.visibility = View.GONE
            binding?.upcommingTitle?.visibility = View.VISIBLE
            binding?.upcommingExercise?.visibility = View.VISIBLE
            if (currentExercisePosition + 1 < exerciseList!!.size) {
                val upcommingExerciseName = exerciseList?.get(currentExercisePosition + 1)!!.name
                binding?.upcommingExercise?.text = upcommingExerciseName
            }
            player?.start()
        }

        restTimer = object : CountDownTimer(countDownValue * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                val timerValue = countDownValue - restProgress
                binding?.progressBar?.progress = timerValue.toInt()
                binding?.tvTimer?.text = timerValue.toString()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                setupRestTimer()
                val rest = !rest
                if (currentExercisePosition + 1 < exerciseList!!.size) {
                    currentExercisePosition += if (rest) 0 else 1
                    setRestProgressBar(
                        rest,
                        if (rest) "GET READY FOR" else exerciseList?.get(currentExercisePosition)!!.name,
                        countDownValue
                    )
                } else {
                    Toast.makeText(this@ExerciseActivity, "Great!!", Toast.LENGTH_LONG).show()
                    speakText("Good Job!")
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val current = LocalDateTime.now().format(formatter)
                    historyViewModel?.addHistory(HistoryEntity(0, current))
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
                if (!rest) {
                    exerciseList?.get(currentExercisePosition)?.isSelected = true
                } else {
                    exerciseList?.get(currentExercisePosition)?.isSelected = false
                    exerciseList?.get(currentExercisePosition)?.isCompleted = true
                }
                exerciseStatusAdaptor?.notifyDataSetChanged()
            }
        }.start()
    }

    private fun setupRestTimer() {
        if (restTimer != null) {
            restTimer?.cancel()
        }
        restTimer = null
        restProgress = 0
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null

        setupRestTimer()
        destroyTTS()
        destroyPlayer()
    }

    private fun destroyPlayer() {
        if (player != null) {
            player?.stop()
        }
    }

    private fun destroyTTS() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
    }

    override fun onInit(status: Int) {
        if (status === TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.ENGLISH)
            if (listOf(TextToSpeech.LANG_MISSING_DATA, TextToSpeech.LANG_NOT_SUPPORTED).contains(
                    result
                )
            ) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakText(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}