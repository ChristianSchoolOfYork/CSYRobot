package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Launchpad {
    Gamepad gamepad2;
    public Launchpad(Gamepad gamepad2){
        this.gamepad2 = gamepad2;
    }

    public Action launchLoop(Action launchPlan, TelemetryPacket p){
        boolean launchBusy = launchPlan != null && launchPlan.run(p);
        return null;
    }
}
