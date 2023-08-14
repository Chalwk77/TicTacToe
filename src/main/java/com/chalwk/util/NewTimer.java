/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.util;

import java.util.Timer;
import java.util.TimerTask;

public class NewTimer {

    /**
     * @param delay The delay in seconds.
     * @param task  The task to be scheduled.
     */
    public void newTimer(int delay, TimerTask task) {
        Timer timer = new Timer();
        timer.schedule(task, 0, delay * 1000L);
    }
}
