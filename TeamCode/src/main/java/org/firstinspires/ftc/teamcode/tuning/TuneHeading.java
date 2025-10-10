package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@SuppressWarnings("unused")
@TeleOp
public class TuneHeading extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TelemetryPacket tp = new TelemetryPacket();
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Heading", Math.toDegrees(drive.localizer.getPose().heading.toDouble()));
            telemetry.update();

            drive.updatePoseEstimate();
            Actions.runBlocking( drive.actionBuilder(drive.localizer.getPose())
                    .turn(Math.toRadians(-90))
                    .waitSeconds(3)
                    .turn(Math.toRadians(90))
                    .waitSeconds(3)
                    .build()
            );
        }
    }
}
