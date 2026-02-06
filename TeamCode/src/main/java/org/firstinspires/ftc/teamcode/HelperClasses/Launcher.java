package org.firstinspires.ftc.teamcode.HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    HardwareMap hardwareMap;
    Servo turnServo, angleServoLeft, angleServoRight, intakeServo;
    DcMotor intakeMotor, launcherMotor;

    public Launcher(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        turnServo = hardwareMap.get(Servo.class,"Turn Servo");
        angleServoLeft = hardwareMap.get(Servo.class, "Angle Servo Left");
        angleServoRight = hardwareMap.get(Servo.class, "Angle Servo Right");
        intakeServo = hardwareMap.get(Servo.class, "Intake Servo");
        intakeMotor = hardwareMap.get(DcMotor.class,"Intake Motor");
        launcherMotor = hardwareMap.get(DcMotor.class, "Launcher Motor");

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //TODO: Reverse Motors If Needed
    }



}
