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

        leftDrive  = hardwareMap.get(DcMotor.class, "leftLauncher");
        rightDrive = hardwareMap.get(DcMotor.class, "rightLauncher");

        waitForStart();

        double leftPower = 0.0;
        boolean is_pressed  = false;

        while (opModeIsActive()) {

            leftDrive.setPower(leftPower);
            rightDrive.setPower(-leftPower);

            if (gamepad1.dpad_up && !is_pressed) {
                leftPower = leftPower + 0.1;
                is_pressed  = true;
            }
            else if (!gamepad1.dpad_up && is_pressed) {
                is_pressed = false;
            }
        }
    }
}
