package com.alerts;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatedAlertDecorator extends AlertDecorator {

    private int repeatCount = 0;
    private int repeat;
    private final int repeatInterval;
    private Timer timer;

    RepeatedAlertDecorator(IAlert alert, int repeatInterval, int repeat) {
        super(alert);
        this.repeatInterval = repeatInterval;
        this.repeat = repeat;
    }

    public void startRepeating() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (repeatCount < repeat) {
                    repeatTriggerAlert();
                }
                else {
                    timer.cancel();
                }
            }
        }, 0, repeatInterval);
    }

    public boolean repeatConditions(IAlert alert){
        boolean repeat  = alert.getClass().equals(ECGAlert.class);
        return repeat;
    }

    public void repeatTriggerAlert() {
        System.out.println("Repeated(" + repeatCount + "): " + getCondition());
        repeatCount++;
    }

}
