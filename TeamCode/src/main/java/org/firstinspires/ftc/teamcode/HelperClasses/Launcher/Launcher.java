package org.firstinspires.ftc.teamcode.HelperClasses.Launcher;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    final protected Servo turnServo, angleServoLeft, angleServoRight, intakeServo;
    final protected DcMotorEx intakeMotor, launcherMotor;
    boolean intakeArmLoaded;

    public Launcher(HardwareMap hardwareMap){
        turnServo = hardwareMap.get(Servo.class,"turnServo");
        angleServoLeft = hardwareMap.get(Servo.class, "angleServoLeft");
        angleServoRight = hardwareMap.get(Servo.class, "angleServoRight");
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        intakeMotor = (DcMotorEx) hardwareMap.get(DcMotor.class,"intakeMotor");
        launcherMotor = (DcMotorEx) hardwareMap.get(DcMotor.class, "launcherMotor");
        intakeArmLoaded = false;

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        angleServoRight.setDirection(Servo.Direction.REVERSE);
        angleServoLeft.setDirection(Servo.Direction.FORWARD);
    }


    //=============================================================
    //Launcher  classes
    public void runLauncher(double power){
        launcherMotor.setPower(power);
    }
    public void runLauncher(){
        launcherMotor.setPower(0.5);
    }
    public void stopLauncher(){
        launcherMotor.setPower(0);
    }
    //=============================================================

    //=============================================================
    //Ball Transit classes
    public boolean isIntakeArmLoaded(){
        return intakeArmLoaded;
    }
    public void toggleArm(){
        final double min = 0,max = 1;
        intakeServo.setPosition(intakeArmLoaded ? max : min);
        intakeArmLoaded = !intakeArmLoaded;
    }
    //=============================================================

    //=============================================================
    //Intake Classes
    public void runIntake(double power){
        intakeMotor.setPower(power);
    }
    public double intakePower(){
        return intakeMotor.getPower();
    }
    //=============================================================

    //=============================================================
    //debug classes
    public void turnIntakeServo(double pos){
        intakeServo.setPosition(pos);
    }
    public double getIntakeServoPos(){
        return intakeServo.getPosition();
    }
    //=============================================================

    //=============================================================
    //Launch angle classes
    public double getAngleRight(){
        return angleServoRight.getPosition();
    }
    protected void turnAngleRight(double position){
        angleServoRight.setPosition(position);
    }
    public double getAngleLeft() {
        return angleServoLeft.getPosition();
    }
    protected void turnAngleLeft(double position){
        angleServoLeft.setPosition(position);
    }
    //=============================================================

    //=============================================================
    //Launcher Rotation Classes
    public void turnLauncher(double speed){
        //Sets speed the new speed, with bounds at 0 and 1
        turnServo.setPosition(Math.max(0, Math.min(1, speed)));
    }
    //=============================================================

    //=============================================================
    //Other classes
    public void emergencyStop(AngleServos angleServos) {
        stopLauncher();
        runIntake(0);
        angleServos.updateTargetSpeed(0.5,true);
        turnLauncher(0.5);
    }
    //=============================================================
}
