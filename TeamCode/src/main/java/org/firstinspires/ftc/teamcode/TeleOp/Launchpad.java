package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import AngleServos;
import org.firstinspires.ftc.teamcode.HelperClasses.Launcher.Launcher;

public class Launchpad {
    Gamepad gamepad2;
    Launcher launcher;
    AngleServos angleServos;
    FtcDashboard dash;
    boolean emergencyStop;
    public Launchpad(Gamepad gamepad2, Launcher launcher, FtcDashboard dash){
        this.gamepad2 = gamepad2;
        this.launcher = launcher;
        this.dash = dash;
        angleServos = new AngleServos(launcher);
        emergencyStop = false;
    }

    public void launchLoop(TelemetryPacket p, Telemetry telemetry){
        emergencyStop = gamepad2.left_stick_button;

        if (emergencyStop){
            launcher.emergencyStop(angleServos);
        } else {
            if (gamepad2.right_stick_button){
                launcher.stopLauncher();
            }else if (gamepad2.startWasPressed()){
                launcher.runLauncher();
            }

            if (gamepad2.aWasPressed()) {
                launcher.toggleArm();
            }

            if (gamepad2.dpadUpWasPressed()){
                launcher.runIntake(Math.max(0,Math.min(1,launcher.intakePower()+0.1)));
            } else if (gamepad2.dpadDownWasPressed()){
                launcher.runIntake(Math.max(0,Math.min(1,launcher.intakePower()-0.1)));
            }
            if(gamepad2.bWasPressed()){
                launcher.runIntake(0);
            }


            if (gamepad2.right_trigger > 0){
                launcher.turnLauncher((gamepad2.right_trigger/2)+0.5);
            } else if (gamepad2.left_trigger > 0){
                launcher.turnLauncher((-gamepad2.left_trigger/2)+0.5);
            }else {
                launcher.turnLauncher(0.5);
            }
            
            angleServos.updateTargetSpeed(0.05 * gamepad2.left_stick_y + 0.5,true);
        }

        dualTelemetry(p, telemetry);
    }

    private void dualTelemetry(TelemetryPacket p, Telemetry telemetry){
        p.put("Intake Arm Loaded: ", launcher.isIntakeArmLoaded()? "Ready":"Wait for loading");
        p.put("Intake Power: ", launcher.intakePower());
        p.put("Angle Left: ", launcher.getAngleLeft());
        p.put("Angle Right: ", launcher.getAngleRight());
        telemetry.addData("Intake Arm Loaded: ", launcher.isIntakeArmLoaded()? "Ready":"Wait for loading");
        telemetry.addData("Intake Power: ", launcher.intakePower());
        telemetry.addData("Angle Left: ", launcher.getAngleLeft());
        telemetry.addData("Angle Right: ", launcher.getAngleRight());
        dash.sendTelemetryPacket(p);
        telemetry.update();
    }

}
