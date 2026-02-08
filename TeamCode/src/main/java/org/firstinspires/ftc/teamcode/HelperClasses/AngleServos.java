package org.firstinspires.ftc.teamcode.HelperClasses;

public class AngleServos {
    private final Launcher launcher;
    private double targetSpeed;

    public AngleServos(Launcher launcher){
        this.launcher = launcher;
        targetSpeed = 0.5;
    }

    /**Increases the target speed by {@code angleIncrease}
     * @param angleIncrease The amount to increase the target angle by
     */
    public void updateTargetSpeed(double angleIncrease, boolean execute){
        targetSpeed = Math.max(0, Math.min(1, targetSpeed + angleIncrease));
        if (execute){
            updateSpeed();
        }
    }

    public double getTargetSpeed(){
        return targetSpeed;
    }

    public void updateSpeed(){
        launcher.turnAngleLeft(targetSpeed);
        launcher.turnAngleRight(targetSpeed);
    }

}
