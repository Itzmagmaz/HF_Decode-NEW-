
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Ezlight Test")
public class Ezlight extends OpMode {

    private Limelight3A limelight;

    final double TARGET_HEIGHT = 5.75;
    final double CAMERA_HEIGHT = 10.0;
    final double MOUNT_ANGLE = 0.0;

    @Override
    public void init() {
        try {
            limelight = hardwareMap.get(Limelight3A.class, "LimeLight3A");
            limelight.pipelineSwitch(0);
            telemetry.addData("Status", "Limelight Found!");
        } catch (Exception e) {
            telemetry.addData("Status", "ERROR: Limelight not found in Config");
        }
    }

    @Override
    public void start() {
        if (limelight != null) {
            limelight.start();
        }
    }

    @Override
    public void loop() {
        if (limelight == null) {
            telemetry.addData("Error", "Check your Robot Configuration name!");
            telemetry.update();
            return;
        }

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            double tx = result.getTx(); // This is your Angle Theta
            double ty = result.getTy();

            double angleToTargetDegree = MOUNT_ANGLE + ty;
            double angleToTargetRadians = Math.toRadians(angleToTargetDegree);
            double distanceInInches = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToTargetRadians);

            telemetry.addData("Theta (Horizontal)", tx);
            telemetry.addData("Distance", Math.abs(distanceInInches));
        } else {
            telemetry.addData("Status", "No Target Detected");
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        if (limelight != null) {
            limelight.stop();
        }
    }
}