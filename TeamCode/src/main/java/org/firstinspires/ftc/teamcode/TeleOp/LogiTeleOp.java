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
import org.firstinspires.ftc.teamcode.HelperClasses.Locationator;
import org.firstinspires.ftc.teamcode.HelperClasses.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Logicool Teleop")
public class LogiTeleOp extends LinearOpMode {

    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //TODO:Update this with values when camera installed
        //Locationator setup
        final Position CAMERA_LOCATION = new Position(DistanceUnit.INCH,0,0,0,0);
        final YawPitchRollAngles CAMERA_ANGLES = new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0);
        AprilTagProcessor aprilTag = new AprilTagProcessor.Builder().setCameraPose(CAMERA_LOCATION,CAMERA_ANGLES).build();

        //Camera Setup
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(aprilTag);
        VisionPortal visionPortal = builder.build();
        FtcDashboard.getInstance().startCameraStream(visionPortal, 30);

        //Drive Setup
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        double lastRepos = time;
        Pose2d pose = new Pose2d(60,-60,0);
        if (!aprilTag.getDetections().isEmpty()) {
            pose = Locationator.getPose(aprilTag);
        }

        //Gamepad setup
        Drivepad game = new Drivepad(gamepad1,drive);

        waitForStart();

        Action drivePlan = null;
        boolean snapTurning = false;
        while (opModeIsActive()) {
            //Create this loop's telemetry packet
            TelemetryPacket p = new TelemetryPacket();

            //update the pose and check for if there is an action going on
            drive.updatePoseEstimate();


            //Update from driver gamepad
            drivePlan = game.loop(pose, drivePlan,p);

            telemetry.addData("Is Running", game.driveRunning);
            //telemetry send
            telemetry.addData("x", pose.position.x);
            telemetry.addData("y", pose.position.y);
            telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
            telemetry.update();
            TelemetryPacket packet = new TelemetryPacket();

            //If it has been more than 5 seconds since we last did it, and robot can see an april tag with location data, update the pose more accurately
            double timeSinceRepos = time - lastRepos;
            if (timeSinceRepos > 5 && !game.driveRunning && !aprilTag.getDetections().isEmpty()){
                pose = Locationator.getPose(aprilTag, packet);
                lastRepos = time;
            }

            //Update Dashboard, not useful in competition
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), pose);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}