package com.mirea.bykonyaia.mireaproject.labs.lab_4;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.Worker;

import java.util.concurrent.TimeUnit;

public class BackgroundWorker extends Worker {
    static final String LOG_TAG = "UploadWorker";
    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "Execution: start");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Execution: end");
        return Result.success();
    }
}

