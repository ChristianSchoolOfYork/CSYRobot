package org.firstinspires.ftc.teamcode.HelperClasses.Camera;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
@Disabled
@TeleOp(name = "Camera Locationator")
public class Locationator extends LinearOpMode {
    private Object math_output;
    //Placement Calculation Function
//
//    public static double Placement_Calc(double range, double elevation) {
//        if (!(range >= elevation)) {
//            throw new IllegalArgumentException("Range must be ≥ Elevation");
//        }
//        return Math.sqrt(range * range - elevation * elevation);
//    }
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        aprilTag = new AprilTagProcessor.Builder().build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
        FtcDashboard.getInstance().startCameraStream(visionPortal, 30);

        waitForStart();
        while (opModeIsActive()){
            TelemetryPacket packet = new TelemetryPacket();
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            packet.put("# AprilTags Detected", currentDetections.size());

            for (AprilTagDetection detection : checkPosTag(currentDetections)){
                Pose3D pos = detection.robotPose;
                packet.addLine(String.format("X: %f Y: %f Z:%f", pos.getPosition().x, pos.getPosition().y,pos.getPosition().z));
            }
            packet.fieldOverlay().setStroke("#3F51B5");
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }

    }


    public static Pose2d getPose(AprilTagProcessor tag) {
        List<AprilTagDetection> currentDetections = checkPosTag(tag.getDetections());
        if (!currentDetections.isEmpty()) {
            AprilTagPoseFtc pos = null;
            pos = checkPosTag(currentDetections).get(0).ftcPose;

            assert pos != null;
            return new Pose2d(pos.x, pos.y, pos.yaw);
        }
        else return null;

    }

    public static Pose2d getPose(AprilTagProcessor tag, TelemetryPacket packet) {
        List<AprilTagDetection> currentDetections = checkPosTag(tag.getDetections());
        packet.put("# AprilTags Detected", currentDetections.size());

        AprilTagPoseFtc pos = null;

        for (AprilTagDetection detection : checkPosTag(currentDetections)) {
            pos = detection.ftcPose;
            packet.addLine(String.format("X: %f Y: %f Yaw:%f", pos.x, pos.y, pos.yaw));
        }

        packet.fieldOverlay().setStroke("#3F51B5");
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
        assert pos != null;
        return new Pose2d(pos.x,pos.y, pos.yaw);

    }


    public static List<AprilTagDetection> checkPosTag(List<AprilTagDetection> currentDetections){
        List<AprilTagDetection> posable = new ArrayList<>();
        for (AprilTagDetection detection: currentDetections){
            if(detection.metadata != null && detection.ftcPose != null){
                posable.add(detection);
            }
        }
        return posable;
    }
}
