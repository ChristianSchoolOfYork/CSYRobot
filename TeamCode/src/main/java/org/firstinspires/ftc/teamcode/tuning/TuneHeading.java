package org.firstinspires.ftc.teamcode.tuning;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Drawing;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PinpointLocalizer;

@SuppressWarnings("unused")
@TeleOp
public class TuneHeading extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive.Params params = new MecanumDrive.Params();
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry1 = dashboard.getTelemetry();

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        drive.updatePoseEstimate();
        telemetry.addData("Heading", GetHeadingInDegrees(drive.localizer.getPose().heading));
        telemetry.update();

        waitForStart();

        boolean isPressed = false;
        int currentRotation = 0;
        Action plan = null;

        while (opModeIsActive()) {
            PinpointLocalizer ppl = (PinpointLocalizer)drive.localizer;
            TelemetryPacket p = new TelemetryPacket();
            drive.updatePoseEstimate();
            Pose2d currentPose = drive.localizer.getPose();

            boolean running = plan != null && plan.run(p);;

            telemetry1.addData("Track width", params.inPerTick * params.trackWidthTicks);
            telemetry1.addData("Ticks per inch", 1.0/params.inPerTick);
            telemetry1.addData("Computed track width", (1.0/params.inPerTick) * 16.0);
            telemetry1.addData("Localizer Heading", GetHeadingInDegrees(currentPose.heading));
            telemetry1.addData("Running", running);
            telemetry1.addData("Current Rotation", currentRotation);
            telemetry1.addData("Current Position", "{X: " + currentPose.position.x + ", Y: " + currentPose.position.y + "}");

            telemetry1.update();

            Canvas fo = p.fieldOverlay();

//            fo.setStroke("red");
//            fo.setStrokeWidth(2);
//            fo.strokeLine(0 , 0, currentPose.heading.real * 10, currentPose.heading.imag * 10);
//
//            fo.setStroke("green");
//            fo.setStrokeWidth(1);
//            fo.strokeCircle(currentPose.position.x, currentPose.position.y, 9);
//            fo.setFill("black");
//            fo.fillCircle(currentPose.position.x, currentPose.position.y, 1);

            Drawing.drawRobot(fo, currentPose);

            if (gamepad1.a && !isPressed && !running)
            {
                currentRotation = (currentRotation + 90);

                if(currentRotation > 360)
                {
                    currentRotation = currentRotation - 360;
                }

                plan = drive.actionBuilder(currentPose)
                        .turn(Math.toRadians(90))
                        .build();

                isPressed = true;
            }
            else if(!gamepad1.a && isPressed)
            {
                isPressed = false;
            }

            dashboard.sendTelemetryPacket(p);
        }
    }

    private double GetHeadingInDegrees(Rotation2d heading) {
       double headingInDegees = Math.atan2(heading.imag, heading.real) * (180 / Math.PI);
       return (headingInDegees + 360) % 360;
    }
}
