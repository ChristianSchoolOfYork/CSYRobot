package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.HelperClasses.AngleServos;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher;

public class Launchpad {
    Gamepad gamepad2;
    Launcher launcher;
    AngleServos angleServos;

    public Launchpad(Gamepad gamepad2, HardwareMap hardwareMap){
        this.gamepad2 = gamepad2;
        this.launcher = new Launcher(hardwareMap);
        angleServos = new AngleServos(launcher);
    }

    public void launchLoop(Action launchPlan, TelemetryPacket p){

    }
}
