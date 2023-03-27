package com.example.eyealarm.alarmslist;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eyealarm.R;
import com.example.eyealarm.data.Alarm;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private ImageView alarmDelete;

    Switch alarmStarted;

    public AlarmViewHolder(@NonNull View itemView){
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmDelete = itemView.findViewById(R.id.item_alarm_delete);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
    }

    public void bind(Alarm alarm, AlarmListener listener){
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());
//        alarmDelete.setImageResource(R.drawable.ic_alarm_black_24dp);

        if(alarm.isRecurring()){
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        }else{
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once off");
        }

        if(alarm.getTitle().length() != 0){
            alarmTitle.setText(alarm.getTitle());
        }else{
            alarmTitle.setText("My alarm");
        }

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onToggle(alarm);
            }
        });

        alarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(alarm);
            }
        });
    }
}
