package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TankDrive;

public final class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();

//            Actions.runBlocking(
//                drive.actionBuilder(beginPose)
//                        .splineTo(new Vector2d(30, 30), Math.PI / 2)
//                        .splineTo(new Vector2d(0, 60), Math.PI)
//                        .splineTo(new Vector2d(-30, 30), -Math.PI / 2)
//                        .splineTo(new Vector2d(0, 0), 0)
//                        .build());
            Actions.runBlocking(drive.actionBuilder(beginPose)
                    .splineTo(new Vector2d(48, 0), Math.toRadians(90))
                    .splineTo(new Vector2d(48, 48), Math.toRadians(180))
                    .splineTo(new Vector2d(0, 48), Math.toRadians(-90))
                    .splineTo(new Vector2d(0, 0), Math.toRadians(0))
                    .build());

//                        Actions.runBlocking(drive.actionBuilder(beginPose)
//                        .lineToXConstantHeading(48)
//                        .turnTo(Math.toRadians(90))
//                        .lineToYConstantHeading(48)
//                        .turnTo(Math.toRadians(180))
//                        .lineToXConstantHeading(0)
//                        .turnTo(Math.toRadians(-90))
//                        .lineToYConstantHeading(0)
//                        .turnTo(0)
//                        .build());

//            Actions.runBlocking(drive.actionBuilder(beginPose)
//                    .splineTo(new Vector2d(48, 0), Math.toRadians(90))
//                    .splineTo(new Vector2d(48, 48), Math.toRadians(180))
//                    .splineTo(new Vector2d(0, 48), Math.toRadians(-90))
//                    .splineTo(new Vector2d(0, 0), Math.toRadians(0))
//                    .build());

        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            TankDrive drive = new TankDrive(hardwareMap, beginPose);

            waitForStart();

            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            .splineTo(new Vector2d(30, 30), Math.PI / 2)
                            .splineTo(new Vector2d(0, 60), Math.PI)
                            .build());
        } else {
            throw new RuntimeException();
        }
    }
}
