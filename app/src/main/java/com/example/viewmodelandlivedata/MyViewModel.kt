package com.example.viewmodelandlivedata

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {

    class MyTimerTask(val initTime: Long, val time: MutableLiveData<Long>) : TimerTask() {
        override fun run() {
            Log.i(TAG, "Time changed")
            val newValue = (SystemClock.elapsedRealtime() - initTime) / 1000
            time.postValue(newValue)
        }

    }

    // Нельзя хранить Context, Activity, Fragment, View, Lifecycle и пр.
    // Может привести к утечкам памяти.

    private val liveTime: MutableLiveData<Long> = MutableLiveData(0)

    init {
        // The init block will execute immediately after the primary constructor.
        Log.d(TAG, "MyViewModel created!")

        val timer = Timer()
        val initTime = SystemClock.elapsedRealtime()
        val task = MyTimerTask(initTime, liveTime)
        timer.scheduleAtFixedRate(task, 1000, 1000)
    }

    var numbers: MutableList<Int> = mutableListOf()
        private set
    private val numberRef: MutableLiveData<MutableList<Int>> = MutableLiveData(numbers)

    private var product: MutableLiveData<Int> = MutableLiveData(0)

    fun add(num: Int) {
        numbers.add(num)
        numberRef.value = numbers
        calcProduct()
    }

    fun remove(idx: Int) {
        numbers.removeAt(idx)
        numberRef.value = numbers
        calcProduct()
    }

    private fun calcProduct() {
        product.value = numbers.reduce { acc, i -> acc * i }
    }

    fun getProduct(): LiveData<Int> {
        return product
    }

    fun getNumbers(): LiveData<MutableList<Int>> {
        return numberRef
    }

    fun getLiveTime(): LiveData<Long> {
        return liveTime
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "MyViewModel cleared!")
    }
}