package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.State;


public interface Action {
    public enum Status {
        IDLE, RUNNING, DONE
    };
    public Status execute();
}
