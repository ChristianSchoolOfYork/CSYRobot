package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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

        while (opModeIsActive()) {
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
