package com.example.eyealarm.createalarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.eyealarm.data.Alarm;
import com.example.eyealarm.data.AlarmRepository;

public class CreateAlarmViewModel extends AndroidViewModel {
    AlarmRepository alarmRepository;

    public CreateAlarmViewModel(@NonNull Application application){
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    public void insert(Alarm alarm){
        alarmRepository.insert(alarm);
    }
}
