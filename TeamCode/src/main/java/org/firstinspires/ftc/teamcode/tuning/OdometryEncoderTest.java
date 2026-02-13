package org.firstinspires.ftc.teamcode.tuning;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation;
import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.zyxOrientation;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.LazyHardwareMapImu;
import com.acmerobotics.roadrunner.ftc.LazyImu;
import com.google.gson.GsonBuilder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
@Disabled
@TeleOp
public class OdometryEncoderTest extends LinearOpMode {

    public GoBildaPinpointDriver driver;

    public final float parResolution = 505.316944406f;
    public final float perpResolution = 336.87796276f;
    public final GoBildaPinpointDriver.EncoderDirection initialParDirection;
    public final GoBildaPinpointDriver.EncoderDirection initialPerpDirection;

    private final float xOffset = 3.75f;

    private final float yOffset = -6.75f;

    public OdometryEncoderTest()
    {
        // TODO: reverse encoder directions if needed
        initialParDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
        initialPerpDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = dashboard.getTelemetry();

        driver = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
 //       IMU imu = hardwareMap.get(IMU.class, "pinpoint");
        driver.setOffsets(xOffset, yOffset, DistanceUnit.INCH);
        driver.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        driver.setEncoderDirections(initialParDirection, initialPerpDirection);

     //   imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(zyxOrientation(0,0,0))));

        LazyImu lazyImu = new LazyHardwareMapImu(hardwareMap, "imu", new RevHubOrientationOnRobot(zyxOrientation(0,0,0)));

        Vector2d lastPosition = new Vector2d(0,0);

        int parDirectionMultiplier = initialParDirection == GoBildaPinpointDriver.EncoderDirection.REVERSED ? -1 : 1;
        int perpDirectionMultiplier = initialPerpDirection == GoBildaPinpointDriver.EncoderDirection.REVERSED ? -1 : 1;

        waitForStart();

        long lastTime = System.nanoTime();
        driver.resetPosAndIMU();
        driver.update();

        int xDrift = driver.getEncoderX();
        int yDrift = driver.getEncoderY();

        while(opModeIsActive())
        {
            TelemetryPacket tp  = new TelemetryPacket();
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            driver.update();

            Vector2d currentRawValues = new Vector2d((driver.getEncoderX() - xDrift) * parDirectionMultiplier, (driver.getEncoderY() - yDrift) * perpDirectionMultiplier);
            Vector2d currentPosition = new Vector2d(currentRawValues.x / parResolution, currentRawValues.y / perpResolution);

            Pose2D pose = driver.getPosition();

            Vector2d movementDelta = currentPosition.minus(lastPosition);

            Vector2d currentVelocity = movementDelta.div(deltaTime);

            telemetry.addData("Current Position X", currentPosition.x);
            telemetry.addData("Current Position Y", currentPosition.y);
            telemetry.addData("Current Velocity", currentVelocity.norm() + "in/sec");

            telemetry.addData("Computer Position X", pose.getX(DistanceUnit.INCH));
            telemetry.addData("Computer Position Y", pose.getY(DistanceUnit.INCH));
            telemetry.addData("Computer Position Y Adjusted", pose.getY(DistanceUnit.INCH) * .667);
            telemetry.addData("Computer Heading", pose.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Unnormalized Radians heading", driver.getHeading(UnnormalizedAngleUnit.RADIANS));
            telemetry.addData("Radians heading", driver.getHeading(AngleUnit.RADIANS));
            telemetry.addData("Lazy IMU heading", lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

            telemetry.update();

            Canvas c = tp.fieldOverlay();

            c.setStroke("green");
            c.setFill("green");
            c.setStrokeWidth(1);
            c.strokeCircle(currentPosition.x, currentPosition.y, 9.0);

            c.setStrokeWidth(1);
            c.fillCircle(currentPosition.x, currentPosition.y, 1);

            c.setStroke("red");
            c.setFill("red");
            c.setStrokeWidth(1);
            c.fillCircle(pose.getX(DistanceUnit.INCH), pose.getY(DistanceUnit.INCH), 2);

            c.setStroke("blue");
            c.setFill("blue");
            c.setStrokeWidth(1);
            c.fillCircle(pose.getX(DistanceUnit.INCH), pose.getY(DistanceUnit.INCH) * .667, 2);

            c.setStroke("black");
            c.setFill("black");
            c.setStrokeWidth(1);
            c.fillCircle(pose.getX(DistanceUnit.INCH), pose.getY(DistanceUnit.INCH) * 1.5, 2);

            c.setStroke("gray");
            c.setFill("gray");
            c.setStrokeWidth(1);
            c.strokeLine(currentPosition.x, currentPosition.y, currentPosition.x + (Math.cos(pose.getHeading(AngleUnit.RADIANS)) * 9), currentPosition.y + (Math.sin(pose.getHeading(AngleUnit.RADIANS))) * 9);

            dashboard.sendTelemetryPacket(tp);

            lastPosition = currentPosition;

        }

    }

    private double GetHeadingInDegrees(Vector2d heading) {
        double headingInDegees = Math.atan2(heading.x, heading.y) * (180 / Math.PI);
        return (headingInDegees + 360) % 360;
    }
}
