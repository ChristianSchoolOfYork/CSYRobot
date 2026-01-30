package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Drivepad {
    Gamepad gamepad1;
    MecanumDrive drive;
    boolean driveRunning;
    public Drivepad(Gamepad gamepad1, MecanumDrive drive){
        this.gamepad1 = gamepad1;
        this.drive = drive;
    }
    public Action loop(Pose2d pose, Action drivePlan, TelemetryPacket p){
        this.driveRunning = drivePlan != null && drivePlan.run(p);
        if (!driveRunning) {
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
                return drive.actionBuilder(pose).setTangent(0).splineToConstantHeading(new Vector2d(39,-39),0).build();
            } else if (gamepad1.b) {
                return drive.actionBuilder(pose).setTangent(0).splineToConstantHeading(new Vector2d(-39,-39),0).build();
            } else if (gamepad1.x) {
                return drivePlan;
            } else if (gamepad1.y){
                return drivePlan;
            } else{
                drive.setDrivePowers(new PoseVelocity2d(new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x), -gamepad1.right_stick_x));
                return drivePlan;
            }
        } else {
            return drivePlan;
        }
    }
}
