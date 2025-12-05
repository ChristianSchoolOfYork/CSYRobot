package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
Controller 1 mapping


 */


@TeleOp(name = "Logicool Teleop")
@SuppressWarnings("unused")
public class LogiTeleOp extends LinearOpMode {
    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        waitForStart();

        Action drivePlan = null;
        boolean snapTurning = false;
        while (opModeIsActive()) {
            PinpointLocalizer ppl = (PinpointLocalizer) drive.localizer;
            TelemetryPacket p = new TelemetryPacket();
            drive.updatePoseEstimate();
            Pose2d pose = drive.localizer.getPose();
            boolean driveRunning = drivePlan != null && drivePlan.run(p);
            telemetry.addData("Is Running", driveRunning);

            //Drive with L and R
            if (!driveRunning){
                drive.setDrivePowers(new PoseVelocity2d(
                        new Vector2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x
                        ),
                        -gamepad1.right_stick_x
                ));
            }

            drivePlan = gamePadOneLoop(driveRunning, drive, pose, drivePlan);

            telemetry.addData("x", pose.position.x);
            telemetry.addData("y", pose.position.y);
            telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), pose);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }

    private Action gamePadOneLoop(boolean isRunning, MecanumDrive drive, Pose2d pose, Action drivePlan){
        if (!isRunning) {
            if (gamepad1.dpad_right) {
               return drive.actionBuilder(pose)
                        .turnTo(Math.toDegrees(-90))
                        .build();
            } else if(gamepad1.dpad_down){
                return drive.actionBuilder(pose)
                        .turnTo(Math.toDegrees(180))
                        .build();
            } else if(gamepad1.dpad_left){
                return drive.actionBuilder(pose)
                        .turnTo(Math.toDegrees(90))
                        .build();
            } else if(gamepad1.dpad_up) {
                return drive.actionBuilder(pose)
                        .turnTo(Math.toDegrees(0))
                        .build();
            } else if(gamepad1.a) {
                return drivePlan;
            } else if (gamepad1.b) {
                return drivePlan;
            } else if (gamepad1.x) {
                return drivePlan;
            } else if (gamepad1.y){
                return drivePlan;
            }
            else
            {
                return drivePlan;
            }
        } else {
            return drivePlan;
        }
    }
}
