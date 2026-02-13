package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Disabled
@TeleOp
public class LauncherTest  extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotor leftDrive;
        DcMotor rightDrive;

        leftDrive = hardwareMap.get(DcMotor.class, "leftLauncher");
        rightDrive = hardwareMap.get(DcMotor.class, "rightLauncher");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        double leftPower = 0.0;
        boolean is_up_pressed = false;
        boolean is_down_pressed = false;

        long lastMilliSeconds = System.nanoTime();
        int lastTicks = leftDrive.getCurrentPosition();
        int lastRightTicks = rightDrive.getCurrentPosition();
        float accum = 0;
        int accumTicks = 0;
        int rightAccumTicks = 0;
        int ticksPerSecond = 0;
        int rightTicksPerSecond = 0;
        long frameNumber = 0;

        while (opModeIsActive()) {
            frameNumber++;
            FtcDashboard dashboard = FtcDashboard.getInstance();
            Telemetry telemetry = dashboard.getTelemetry();

            long currentMilliSeconds = System.nanoTime();
            long elapsedMillis = currentMilliSeconds - lastMilliSeconds;
            float elapsedSeconds = elapsedMillis * 0.000000001f;
            lastMilliSeconds = currentMilliSeconds;

            int currentTicks = leftDrive.getCurrentPosition();
            int rightCurrentTicks = rightDrive.getCurrentPosition();

            accumTicks += currentTicks - lastTicks;
            rightAccumTicks += rightCurrentTicks - lastRightTicks;
            lastTicks = currentTicks;
            lastRightTicks = rightCurrentTicks;

            accum += elapsedSeconds;

            if(accum >= 1)
            {
                ticksPerSecond = accumTicks;
                rightTicksPerSecond = rightAccumTicks;
                accumTicks = 0;
                rightAccumTicks = 0;
                accum = accum - 1;
            }

            telemetry.addData("Frame critical time in seconds", accum);
            telemetry.addData("Left ticks per second", Math.abs(ticksPerSecond));
            telemetry.addData("Right ticks per second", Math.abs(rightTicksPerSecond));
            telemetry.addData("Power level", leftPower);

            leftDrive.setPower(leftPower);
            rightDrive.setPower(-leftPower);

            if (gamepad1.dpad_up && !is_up_pressed) {
                leftPower = leftPower + 0.1;
                is_up_pressed = true;
            } else if (!gamepad1.dpad_up && is_up_pressed) {
                is_up_pressed = false;
            }

            if (gamepad1.dpad_down && !is_down_pressed) {
                leftPower = leftPower - 0.1;
                is_down_pressed = true;
            } else if (!gamepad1.dpad_down && is_down_pressed) {
                is_down_pressed = false;
            }

            if (gamepad1.b) {
                leftPower = 0.0;
            }

            telemetry.update();
        }
    }
}
