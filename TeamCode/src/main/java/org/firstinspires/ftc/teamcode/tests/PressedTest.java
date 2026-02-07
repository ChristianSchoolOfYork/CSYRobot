package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.TeleOp.Launchpad;


@TeleOp
public class PressedTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry.addData("waiting for start", true);
        Launchpad launchpad = new Launchpad(gamepad2, hardwareMap);
        waitForStart();
        TelemetryPacket p = new TelemetryPacket();

        while (opModeIsActive()){
            launchpad.launchLoop(null,p);
            dashboard.sendTelemetryPacket(p);
        }

    }
}
