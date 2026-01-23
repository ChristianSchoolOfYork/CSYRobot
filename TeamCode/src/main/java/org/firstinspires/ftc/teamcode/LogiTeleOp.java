package org.firstinspires.ftc.teamcode;
import android.annotation.SuppressLint;
import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Logicool Teleop")
public class LogiTeleOp extends LinearOpMode {

    private Object math_output;
    //Placement Calculation Function
    public static double Placement_Calc(double range, double elevation) {
        if (!(range >= elevation)) {
            throw new IllegalArgumentException("Range must be ≥ Elevation");
        }
        return Math.sqrt(range * range - elevation * elevation);
    }

    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //TODO:Update this with values when camera installed
        final Position CAMERA_LOCATION = new Position(DistanceUnit.INCH,0,0,0,0);
        final YawPitchRollAngles CAMERA_ANGLES = new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0);
        AprilTagProcessor aprilTag = new AprilTagProcessor.Builder().setCameraPose(CAMERA_LOCATION,CAMERA_ANGLES).build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(aprilTag);
        VisionPortal visionPortal = builder.build();
        FtcDashboard.getInstance().startCameraStream(visionPortal, 30);

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        double lastRepos = time;
        Pose2d pose = new Pose2d(60,-60,0);
        if (!aprilTag.getDetections().isEmpty()) {
            pose = Locationator.getPose(aprilTag);
        }

        waitForStart();

        Action drivePlan = null;
        boolean snapTurning = false;
        while (opModeIsActive()) {
            PinpointLocalizer ppl = (PinpointLocalizer) drive.localizer;
            TelemetryPacket p = new TelemetryPacket();
            drive.updatePoseEstimate();
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
//            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//            packet.put("# AprilTags Detected", currentDetections.size());
//
//            for (AprilTagDetection detection : currentDetections) {
//                if (detection.metadata != null && detection.ftcPose != null) {
//                    packet.addLine(String.format("==== (ID %d) %s", detection.id, detection.metadata.name));
//                    if (!detection.metadata.name.contains("Obelisk")) {
//                        packet.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)",
//                                detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
//                        packet.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)",
//                                detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
//                        packet.addLine(String.format("Range %6.1f  Bearing %6.1f  Elevation %6.1f",
//                                detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
//
//                        // Fixed: Calculate virtual (horizontal) distance inside the loop
//                        double virtualDistance = Placement_Calc(detection.ftcPose.range, detection.ftcPose.elevation);
//                        packet.addLine(String.format("Virtual Distance %6.1f in", virtualDistance));
//
//                        // Optional: also send to regular telemetry if you want it on driver station
//                        telemetry.addData("Virtual Distance (ID " + detection.id + ")", virtualDistance);
//                    }
//                } else {
//                    packet.addLine(String.format("==== (ID %d) Unknown", detection.id));
//                    packet.addLine(String.format("Center %6.0f %6.0f   (pixels)",
//                            detection.center.x, detection.center.y));
//                }
//            }
            double timeSinceRepos = time - lastRepos;
            if (timeSinceRepos > 5 && !driveRunning && !aprilTag.getDetections().isEmpty()){
                pose = Locationator.getPose(aprilTag, packet);
                lastRepos = time;
            }
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
                return drive.actionBuilder(pose).setTangent(0).splineToConstantHeading(new Vector2d(39,-39),0).build();
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