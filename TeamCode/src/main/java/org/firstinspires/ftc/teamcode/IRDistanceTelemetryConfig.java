package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.tuning.AllianceSelector; // <-- import the selector

@TeleOp(name = "IRDistanceTelemetryConfig", group = "Sensor")
public class IRDistanceTelemetryConfig extends LinearOpMode {

    // Total width between walls in inches
    private static final double TOTAL_WIDTH = 140.0;
    private static final int RED_ALLIANCE = 0;
    private static final int BLUE_ALLIANCE = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        // Connect to distance sensors
        DistanceSensor leftIR  = hardwareMap.get(DistanceSensor.class, "leftIR");
        DistanceSensor rightIR = hardwareMap.get(DistanceSensor.class, "rightIR");

        telemetry.addLine("Sensors initialized");
        telemetry.update();

        // Use AllianceSelector instead of inline loop
        AllianceSelector selector = new AllianceSelector();
        int currentAlliance = selector.selectAlliance(gamepad1, telemetry, this);

        waitForStart();

        // Get dashboard instance
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        while (opModeIsActive()) {

            // Get distances from sensors in inches
            double leftDist  = leftIR.getDistance(DistanceUnit.INCH);
            double rightDist = rightIR.getDistance(DistanceUnit.INCH);

            // Calculate robot's X position relative to the left wall
            double robotX = 0;

            if (leftIR.getDistance(DistanceUnit.INCH) > 50) {
                robotX = (-70) - (rightIR.getDistance(DistanceUnit.INCH));
            } else {
                robotX = (70 - (leftIR.getDistance(DistanceUnit.INCH)));
            }

            // Telemetry output
            dashboardTelemetry.addData("Left IR (in)", leftDist);
            dashboardTelemetry.addData("Right IR (in)", rightDist);
            dashboardTelemetry.addData("Robot Y position (in)", robotX);
            dashboardTelemetry.addData("Alliance", currentAlliance == 0 ? "Red" : "Blue"); // show alliance
            dashboardTelemetry.update();

            telemetry.addData("Left IR (in)", leftDist);
            telemetry.addData("Right IR (in)", rightDist);
            telemetry.addData("Robot Y position (in)", robotX);
            telemetry.addData("Alliance", currentAlliance == 0 ? "Red" : "Blue"); // show alliance
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), new Pose2d(robotX, 70, Math.toRadians(-90)));
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
