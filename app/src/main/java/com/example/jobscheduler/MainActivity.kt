package com.example.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jobscheduler.databinding.ActivityMainBinding

/*
    setting up a job scheduler is just a 3 step process
    1. create job service class, we also have to registered that jobService class in manifest
    2. then we have to create job info object where we will define under what Circumstance this job will execute
    3. then we schedule the job.

    Job scheduler run on a UI thread i.e. Main thread
 */

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var jobId = 243

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.start.setOnClickListener {
            val componentName: ComponentName = ComponentName(this, OurJobService::class.java)
            val jobInfo: JobInfo = JobInfo.Builder(jobId, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)     // here we are defining we only want to run this job when the phone is
                                                                            // connected to the wifi, there are other number of attribute which we can use
                .setPersisted(true) // this will keep our job alive even if we reboot the device
                //.setPeriodic(15 * 60 * 100) // here we define how often we want to re-call this job i.e. repeat, the default and min value is 15min, but its not exact.
                .build()

            val jobScheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val result = jobScheduler.schedule(jobInfo)

            if(result == JobScheduler.RESULT_SUCCESS)
                Log.i("here22", "Job Scheduled successfully")
            else
                Log.i("here22", "Job was not scheduled something went wrong")
        }

        binding.stop.setOnClickListener {
            val jobScheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(jobId)
            Log.i("here22", "forcefully canceled the job")
        }
    }
}