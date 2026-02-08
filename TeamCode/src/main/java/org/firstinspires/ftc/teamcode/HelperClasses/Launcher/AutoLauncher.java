package org.firstinspires.ftc.teamcode.HelperClasses.Launcher;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;



public class AutoLauncher extends Launcher{
    public AutoLauncher(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    public Action startLauncher() {
        return new Action() {
            private boolean init = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    runLauncher();
                    init = true;
                }

                double vel = launcherMotor.getVelocity();
                return vel > 10_000.0;
            }
        };
    }

}
