package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@TeleOp
public class TuneHeading extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
    @Override
    public void runOpMode() throws InterruptedException {
        while (opModeIsActive()) {
            Actions.runBlocking(
                    drive.actionBuilder(new Pose2d(0, 0, 0))
                            .turnTo(Math.PI)
                            .turnTo(0)
                            .build());
        }
    }
}
