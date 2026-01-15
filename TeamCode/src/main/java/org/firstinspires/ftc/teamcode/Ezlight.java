package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Ezlight Test")
public class Ezlight extends OpMode {

    private Limelight3A limelight;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight"); // name must match Robot Config
        // limelight.start();  // depends on how you’re using it
    }

    @Override
    public void loop() {
        // read results / telemetry here
    }
}