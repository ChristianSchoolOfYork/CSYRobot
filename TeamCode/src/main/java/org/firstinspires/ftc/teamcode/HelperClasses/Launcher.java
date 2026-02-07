package org.firstinspires.ftc.teamcode.HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    final private Servo turnServo, angleServoLeft, angleServoRight, intakeServo;
    final private DcMotor intakeMotor, launcherMotor;
    boolean intakeStage;

    public Launcher(HardwareMap hardwareMap){
        turnServo = hardwareMap.get(Servo.class,"Turn Servo");
        angleServoLeft = hardwareMap.get(Servo.class, "Angle Servo Left");
        angleServoRight = hardwareMap.get(Servo.class, "Angle Servo Right");
        intakeServo = hardwareMap.get(Servo.class, "Intake Servo");
        intakeMotor = hardwareMap.get(DcMotor.class,"Intake Motor");
        launcherMotor = hardwareMap.get(DcMotor.class, "Launcher Motor");
        intakeStage = false;

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //TODO: Reverse Motors If Needed
        //TODO: Find Which Servo Needs To Be Reversed (assuming right for now)
        angleServoRight.setDirection(Servo.Direction.REVERSE);
    }

    //Launcher related classes
    public void launch(double power){
        launcherMotor.setPower(power);
    }
    public void launch(){
        launcherMotor.setPower(1);
    }
    public void holdLaunch(){
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
        intakeServo.setPosition(intakeStage? 1 : 0);
        intakeStage = !intakeStage;
    }

    //Launch angle classes (private because they are to be accessed through the angle servos class anywhere else, as a pair
    protected void angleRight(double position){
        angleServoRight.setPosition(position);
    }
    protected double getAngleLeft(){
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
