package org.firstinspires.ftc.teamcode.HelperClasses.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class LogiCamera {

    public static AprilTagProcessor initCamera(HardwareMap hardwareMap) {
        //TODO:Update this with values when camera installed


        final Position CAMERA_LOCATION = new Position(DistanceUnit.INCH,0,0,0,0);
        final YawPitchRollAngles CAMERA_ANGLES = new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0);
        AprilTagProcessor tagCam = new AprilTagProcessor.Builder().setCameraPose(CAMERA_LOCATION,CAMERA_ANGLES).build();


        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(tagCam);
        VisionPortal visionPortal = builder.build();
        FtcDashboard.getInstance().startCameraStream(visionPortal, 30);

        return tagCam;
    }

}
