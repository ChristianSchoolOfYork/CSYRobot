package org.firstinspires.ftc.teamcode.AutoOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.HelperClasses.Camera.Locationator;
import org.firstinspires.ftc.teamcode.HelperClasses.Camera.LogiCamera;
import org.firstinspires.ftc.teamcode.HelperClasses.Driving.MecanumDrive;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher.AutoLauncher;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher.Launcher;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "Logicool Auto", group = "Logicool",preselectTeleOp = "Logicool Teleop")
public class LogiAutoOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dash = FtcDashboard.getInstance();
        TelemetryPacket p = new TelemetryPacket();

        Telemetry telem = new MultipleTelemetry(telemetry, dash.getTelemetry());

        boolean isRedAlliance;
        if (blackboard.get("alliance") == null) {
            isRedAlliance = true;
        } else {
            isRedAlliance = (boolean) blackboard.get("alliance");
        }

        telem.addData("Alliance: ", blackboard.get("alliance"));


        AprilTagProcessor aprilTag = LogiCamera.initCamera(hardwareMap);

        Pose2d startingPose = isRedAlliance ? new Pose2d(60, -60, 0) : new Pose2d(-60, -60, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);
        Pose2d pose = drive.localizer.getPose();
        if (!aprilTag.getDetections().isEmpty()) {
            pose = Locationator.getPose(aprilTag);
            drive.localizer.setPose(pose);
        }
        AutoLauncher launcher = new AutoLauncher(hardwareMap);
        waitForStart();
        //===================================================
        //START

        Actions.runBlocking(launcher.startLauncher());



    }
}
