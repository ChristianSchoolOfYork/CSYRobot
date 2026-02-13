package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher.*;

public class Launchpad {
    Gamepad gamepad2;
    Launcher launcher;
    AngleServos angleServos;
    FtcDashboard dash;
    boolean emergencyStop;
    final double INC_INTAKE = 0.1, INC_LAUNCHER = 0.05;
    double incArm;
    public Launchpad(Gamepad gamepad2, Launcher launcher, FtcDashboard dash){
        this.gamepad2 = gamepad2;
        this.launcher = launcher;
        this.dash = dash;
        emergencyStop = false;
        incArm = 0.08;
    }

    public void launchLoop(TelemetryPacket p, Telemetry telemetry){
        emergencyStop = gamepad2.left_stick_button;

        if (emergencyStop){
            launcher.emergencyStop();
        } else {
            if (gamepad2.right_stick_button){
                launcher.stopLauncher();
            }else if (gamepad2.yWasPressed()){
                launcher.runLauncher();
            }

            if (gamepad2.left_bumper) {
                if (gamepad2.dpadUpWasPressed()) {
                    launcher.runIntake(Math.max(0, Math.min(1, launcher.intakePower() + INC_INTAKE)));
                } else if (gamepad2.dpadDownWasPressed()) {
                    launcher.runIntake(Math.max(0, Math.min(1, launcher.intakePower() - INC_INTAKE)));
                }
            } else if (gamepad2.right_bumper) {
                if (gamepad2.dpadUpWasPressed()) {
                    launcher.runLauncher(Math.max(0, Math.min(0.75, launcher.launcherPower() + INC_LAUNCHER)));
                } else if (gamepad2.dpadDownWasPressed()) {
                    launcher.runLauncher(Math.max(0, Math.min(0.75, launcher.launcherPower() - INC_LAUNCHER)));
                }
            }else if (gamepad2.x) {
                if (gamepad2.dpadUpWasPressed()) {
                    launcher.turnIntakeServo(0.68);
                } else if (gamepad2.dpadDownWasPressed()) {
                    launcher.turnIntakeServo(0.4);
                } else if (gamepad2.dpadLeftWasPressed() || gamepad2.dpadRightWasPressed()){
                    launcher.holdBall();
                }
            } else if (gamepad2.start){
                if (gamepad2.dpadUpWasPressed()) {
                    incArm *= 2;
                } else if (gamepad2.dpadDownWasPressed()) {
                    incArm /= 2;
                }
            }
            if(gamepad2.bWasPressed()){
                launcher.runIntake(0);
            }

            if (gamepad2.left_trigger > 0.9 && gamepad2.right_trigger > 0.9 && gamepad2.left_stick_y > 0.9 && gamepad2.right_stick_y > 0.9){
                launcher.emergencyStart();
            }
        }

        dualTelemetry(p, telemetry);
    }

    private void dualTelemetry(TelemetryPacket p, Telemetry telemetry){
        p.put("Intake Arm Loaded: ", launcher.isIntakeArmLoaded()? "Ready":"Wait for loading");
        p.put("Intake Power: ", launcher.intakePower());
        p.put("Launcher power", launcher.launcherPower());
        telemetry.addData("Intake Power: ", launcher.intakePower());
        telemetry.addData("Launcher power", launcher.launcherPower());
        telemetry.addData("Arm pos: ", launcher.getIntakeServoPos());
        telemetry.addData("Arm Inc: ", incArm);

    }

}
