package com.example.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class OurJobService(): JobService() {
    private var jobCancelled = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i("here22", "Job started.....")

        /*
            if you our task is small can be ruined in this scope then we have to return false, this tells the system that
            this jobs is over.

            Now if there are any long running task which we want to execute, for that we have to create our own thread here since the by default
            job scheduler runs on the main thread. And in this case we have to run true inorder to let the system know this app still needs to be alive as
            its running some tasks in the background. But than it also mean that we are responsible to let the system know when we stop this process.
         */
        doSomeWorkOnBackground(params)
        return true
    }

    /*
        this method only gets called when our jobs gets canceled e.g. we have set a job which will
        run only when the phone is on charge, but lets say user plugin in the charger and then suddenly remove it, at that point this method
        will get called.

        And we can return true if we want our job to be re-scheduled and false for the otherwise

        If we return true, we are then responsible to stop our background work, since its on a separate thread.
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i("here22", "Job got canceled before it could finish")
        jobCancelled = true
        return true
    }

    private fun doSomeWorkOnBackground(params: JobParameters?) {
        Thread(Runnable {
            for(i in 1..10) {
                if(jobCancelled)
                    return@Runnable

                Log.i("here22", "Background task running... $i")
                Thread.sleep(2000) // i.e. 2 seconds
            }

            Log.i("here22", "Job finished, now we have to let the system know that our task is over")
            jobFinished(params, false) // we will pass true if we want to re-scheduler our job which is only important if something fails
        }).start()
    }
}