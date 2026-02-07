package org.firstinspires.ftc.teamcode.HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.commands.ServoCommands;

public class Launcher {
    final private Servo turnServo, angleServoLeft, angleServoRight, intakeServo;
    final private DcMotor intakeMotor, launcherMotor;
    boolean intakeStage;

    public Launcher(HardwareMap hardwareMap){
        turnServo = hardwareMap.get(Servo.class,"turnServo");
        angleServoLeft = hardwareMap.get(Servo.class, "angleServoLeft");
        angleServoRight = hardwareMap.get(Servo.class, "angleServoRight");
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        intakeMotor = hardwareMap.get(DcMotor.class,"intakeMotor");
        launcherMotor = hardwareMap.get(DcMotor.class, "launcherMotor");
        intakeStage = false;

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        angleServoRight.setDirection(Servo.Direction.REVERSE);
        angleServoLeft.setDirection(Servo.Direction.FORWARD);
    }

    //Launcher related classes
    public void launch(double power){
        launcherMotor.setPower(power);
    }
    public void launch(){
        launcherMotor.setPower(1);
    }
    public void stopLaunch(){
        launcherMotor.setPower(0);
    }

    //Intake Classes
    public boolean getLaunchStage(){
        return intakeStage;
    }
    public void runIntake(double power){
        intakeMotor.setPower(power);
    }
    public void nextLaunchStage(){
        final double min = 0,max = 1;
        intakeServo.setPosition(intakeStage? max : min);
        intakeStage = !intakeStage;
    }

    //debug classes
    public void setIntakeServo(double pos){
        intakeServo.setPosition(pos);
    }

    public double getIntakeServoPos(){
        return intakeServo.getPosition();
    }

    //Launch angle classes (private because they are to be accessed through the angle servos class anywhere else, as a pair
    public double getAngleRight(){
        return angleServoRight.getPosition();
    }

    protected void angleRight(double position){
        angleServoRight.setPosition(position);
    }

    public double getAngleLeft(){
        return angleServoLeft.getPosition();
    }
    protected void angleLeft(double position){
        angleServoLeft.setPosition(position);
    }

    //Launcher Rotation Classes
    public void rotateLauncher(double positonUpdate){
        //Sets position to the current position + the new position, with bounds at 0 and 1
        turnServo.setPosition(Math.max(0, Math.min(1,turnServo.getPosition() + positonUpdate)));
    }

}
