package org.firstinspires.ftc.teamcode.HelperClasses;

public class AngleServos {
    private final Launcher launcher;
    private double targetAngle;

    public AngleServos(Launcher launcher){
        this.launcher = launcher;

        //TODO Update This If Servos Are Flipped
        targetAngle = launcher.getAngleLeft();
    }

    /**Increases the target angle by {@code angleIncrease}
     * @param angleIncrease The amount to increase the target angle by
     */
    public void updateTargetAngle(double angleIncrease){
        targetAngle = Math.max(0, Math.min(1, targetAngle + angleIncrease));
    }

    public double getTargetAngle(){
        return targetAngle;
    }

    public void updateAngle(){
        launcher.angleLeft(targetAngle);
        launcher.angleRight(targetAngle);
    }

}
