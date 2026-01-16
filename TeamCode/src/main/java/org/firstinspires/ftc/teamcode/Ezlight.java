package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.teamcode.mechanisms.TestBench;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name="Ezlight Test")
public class Ezlight extends OpMode {

    private Limelight3A limelight;


    private double distance;



    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight"); // name must match Robot Config
        limelight.pipelineSwitch(0); // pipeline 0 is the april tag as of 1/15/26
        // limelight.start();  // depends on how you’re using it. btw limelight uses a lot of energy so becareful
    }


    @Override
    public void start() {
        limelight.start();
    }


    @Override
    public void loop() {



        LLResult llResult = limelight.getLatestResult();
        if (llResult != null & llResult.isValid())
        {
          distance = distanceCalc(llResult.getTa());
          telemetry.addData("distance: ", distance); // temporrary so no error yay
        }
        // read results / telemetry here
    }

    public double distanceCalc(double target)  /* this is temporarily put in here for testing, however i cannot find this class when trying it on the control hub.*/
    {
        double scale = 30665.95/2.54;  //converting it to inches from centimeters
        double distance = (scale/target);
        return distance;
    }

}