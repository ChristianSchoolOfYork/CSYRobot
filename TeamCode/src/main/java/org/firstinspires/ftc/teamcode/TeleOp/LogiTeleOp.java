package org.firstinspires.ftc.teamcode.TeleOp;
import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Drawing;
import org.firstinspires.ftc.teamcode.HelperClasses.Camera.LogiCamera;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher.Launcher;
import org.firstinspires.ftc.teamcode.HelperClasses.Camera.Locationator;
import org.firstinspires.ftc.teamcode.HelperClasses.Driving.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Logicool Teleop",group = "Logicool")
public class LogiTeleOp extends LinearOpMode {

    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        AprilTagProcessor aprilTag = LogiCamera.initCamera(hardwareMap);

        //Drive Setup
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        double lastRepos = time;
        Pose2d pose = new Pose2d(60,-60,0);
        if (!aprilTag.getDetections().isEmpty()) {
            pose = Locationator.getPose(aprilTag);
        }

        //Launcher Setup
        Launcher launcher = new Launcher(hardwareMap);

        //Gamepad setup
        Drivepad drivepad = new Drivepad(gamepad1,drive);
        Launchpad launchpad = new Launchpad(gamepad2, launcher, FtcDashboard.getInstance());
        waitForStart();


        Action drivePlan = null;
        boolean snapTurning = false;
        while (opModeIsActive()) {
            //Create this loop's telemetry packet
            TelemetryPacket p = new TelemetryPacket();

            //update the pose
            drive.updatePoseEstimate();


            //Update from driver gamepad
            drivePlan = drivepad.loop(pose, drivePlan, p);

            launchpad.launchLoop(p, telemetry);

            telemetry.addData("Is Running", drivepad.driveRunning);
            telemetry.addData("x", pose.position.x);
            telemetry.addData("y", pose.position.y);
            telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
            telemetry.addData("Left X: ", gamepad1.left_stick_x);
            telemetry.addData("left Y: ", gamepad1.left_stick_y);
            telemetry.update();
            TelemetryPacket packet = new TelemetryPacket();

            //If it has been more than 5 seconds since we last did it, and robot can see an april tag with location data, update the pose more accurately
            double timeSinceRepos = time - lastRepos;
            if (timeSinceRepos > 5 && !drivepad.driveRunning && !aprilTag.getDetections().isEmpty()){
                pose = Locationator.getPose(aprilTag, packet);
                lastRepos = time;
            }

            //Update Dashboard, not useful in competition
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), pose);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
        launcher.emergencyStop();
    }
}