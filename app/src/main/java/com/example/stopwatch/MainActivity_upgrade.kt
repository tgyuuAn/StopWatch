package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity_upgrade : AppCompatActivity() , View.OnClickListener{
    private lateinit var btn_start : Button
    private lateinit var btn_reset : Button
    private lateinit var tv_minute : TextView
    private lateinit var tv_second : TextView
    private lateinit var tv_millisecond : TextView
    private var isRunning = false
    private var timer : Timer? = null
    private var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_upgrade)
        btn_start = findViewById(R.id.btn_start)
        btn_reset = findViewById(R.id.btn_reset)
        tv_minute = findViewById(R.id.tv_minute)
        tv_second = findViewById(R.id.tv_second)
        tv_millisecond = findViewById(R.id.tv_millisecond)

        btn_start.setOnClickListener(this)
        btn_reset.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_start -> {
                if(isRunning){
                    pause()
                }else{
                    start()
                }
            }
            R.id.btn_reset -> {
                reset()
            }
        }
    }

    fun pause(){
        btn_start.text = getString(R.string.btn_start_eng)

        isRunning = false
        timer?.cancel()

    }
    fun start(){
        btn_start.text = getString(R.string.btn_pause_eng)
        //이부분 까지는 메인 스레드에서 동작하기 떄문에 UI가 변경되어도 상관 없음.

        isRunning = true

        timer = timer(period = 10){
            time++
            val milli_second = time%100
            val second = (time%6000)/100
            val minute = time/6000

            runOnUiThread{
                if(isRunning){
                    tv_millisecond.text =if(milli_second<10) ".0${milli_second}" else ".${milli_second}"
                    tv_second.text = if(second<10) ":0${second}" else ":${second}"
                    tv_minute.text = if(minute<10) "0${minute}" else "${minute}"
            }
                //if문을 안넣어주면, 스톱워치가 동작하고 있을 때 reset()이 실행되면 텍스트 변경되는게 겹치게 되버림.

            }// timer는 백그라운드 스레드에서 동작하기떄문에,
            //백그라운드 스레드에서 UI를 변경할 수는 없어서 바로 꺼짐.
            //이를 방지하기 위해서 runOnUiThread를 사용함.
        }

    }
    fun reset(){
        isRunning = false
        timer?.cancel()
        time = 0
        btn_start.text = getString(R.string.btn_start_eng)
        tv_millisecond.text = ",00"
        tv_second.text = ":00"
        tv_minute.text = "00"
    }
}