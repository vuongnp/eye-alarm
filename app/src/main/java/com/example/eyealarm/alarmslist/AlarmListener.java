package com.example.eyealarm.alarmslist;

import com.example.eyealarm.data.Alarm;

public interface AlarmListener {
    void onToggle(Alarm alarm);

    void onDelete(Alarm alarm);
}
