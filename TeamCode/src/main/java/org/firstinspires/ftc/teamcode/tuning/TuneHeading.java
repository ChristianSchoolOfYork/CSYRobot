package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@SuppressWarnings("unused")
@TeleOp
public class TuneHeading extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TelemetryPacket tp = new TelemetryPacket();
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        drive.updatePoseEstimate();
        telemetry.addData("Heading", GetHeadingInDegrees(drive.localizer.getPose().heading));
        telemetry.update();

        waitForStart();


        boolean runOnce = false;
        boolean isPressed = false;
        int currentRotation = 0;

        while (opModeIsActive()) {
            drive.updatePoseEstimate();
            telemetry.addData("Localizer Heading", GetHeadingInDegrees(drive.localizer.getPose().heading));
            telemetry.addData("LazyIMU Yaw value", drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("Run once", runOnce);
            telemetry.addData("Is Pressed", isPressed);
            telemetry.addData("Current Rotation", currentRotation);
            telemetry.addData("Radians for 270", Math.toRadians(270));
            telemetry.addData("X (real) component", Rotation2d.exp(Math.toRadians(270)).real);
            telemetry.addData("Y (imag) component", Rotation2d.exp(Math.toRadians(270)).imag);
            telemetry.update();

            if(runOnce)
            {
                currentRotation = (currentRotation + 90);

                if(currentRotation > 360)
                {
                    currentRotation = currentRotation - 360;
                }

                boolean running = true;
                Pose2d pose = drive.localizer.getPose();

                Action plan = drive.actionBuilder(new Pose2d(0,0,0))
                        .turnTo(Math.toRadians(-90))
                        .build();

                int loopNumber = 0;

                while(running && !isStopRequested()) {
                    TelemetryPacket t = new TelemetryPacket();
                    running = plan.run(t);
                    telemetry.addData("Loop number",loopNumber++);
                    telemetry.addData("Heading: ", GetHeadingInDegrees(drive.localizer.getPose().heading));
                    telemetry.update();
                }

                runOnce = false;
            }

            if (gamepad1.a && !isPressed && !runOnce)
            {
                isPressed = true;
                runOnce = true;
            }
            else if(!gamepad1.a && isPressed)
            {
                isPressed = false;
            }
        }
    }

    private double GetHeadingInDegrees(Rotation2d heading) {
       double headingInDegees = Math.atan2(heading.imag, heading.real) * (180 / Math.PI);
       return (headingInDegees + 360) % 360;
    }
}
