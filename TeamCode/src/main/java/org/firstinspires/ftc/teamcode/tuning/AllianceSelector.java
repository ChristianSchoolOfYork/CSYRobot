package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AllianceSelector {
    private int currentAlliance = -1;   // -1 = not chosen, 0 = Red, 1 = Blue
    private int currentSelection = 0;   // 0 = Red, 1 = Blue

    public int selectAlliance(Gamepad gamepad1, Telemetry telemetry, LinearOpMode opMode) {
        while (!opMode.isStopRequested() && currentAlliance == -1) {
            // Show menu
            telemetry.addData(currentSelection == 0 ? "> Red Alliance" : "  Red Alliance", "");
            telemetry.addData(currentSelection == 1 ? "> Blue Alliance" : "  Blue Alliance", "");
            telemetry.update();

            // Handle input
            if (gamepad1.dpad_up) {
                currentSelection = currentSelection == 0 ? 1 : 0;
            } else if (gamepad1.dpad_down) {
                currentSelection = currentSelection == 1 ? 0 : 1;
            } else if (gamepad1.a) {
                currentAlliance = currentSelection;
            }
        }

        // Confirm choice
        telemetry.addData("Selected Alliance: ", currentAlliance == 0 ? "Red" : "Blue");
        telemetry.update();
        OpMode.blackboard.put("alliance", currentAlliance == 0);

        return currentAlliance;
    }
}
