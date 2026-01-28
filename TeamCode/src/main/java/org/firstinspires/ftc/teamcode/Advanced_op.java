package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name = "Advanced_Op", group ="OpMode")
public class Advanced_op extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null; // 475 rpm
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor FL, BL, BR, FR;
    private DcMotor shooter1 = null; // controls flywheel 6000rpm
    private DcMotor shooter2 = null; // controls flywheel 6000rpm
    private DcMotor intake = null; // intakes balls
    private DcMotor kick = null; // Will lift the robot up
    private Servo turret = null; // controls rotation of the turret, 1150 rpm
    private IMU imu = null; //Might break from use
    private AltHardware altHardware;
    private enum ShooterMode { OFF, SOFT, HARD }
    private ShooterMode shooterMode = ShooterMode.OFF;
    private boolean prevCircle = false;
    private boolean prevTriangle = false;
    private boolean prevSquare = false;
    private boolean prevSlowMode = false;
    private boolean intakeOn = false;
    private boolean slowModeOn = false;



    //need to add Odementry here

    private Limelight3A limelight;


    @Override
    public void init() {
        altHardware = new AltHardware(hardwareMap);
        FL = leftFrontDrive = hardwareMap.get(DcMotor.class, "FL");
        BL = leftBackDrive = hardwareMap.get(DcMotor.class, "BL");
        FR = rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        BR = rightBackDrive = hardwareMap.get(DcMotor.class, "BR");
        intake = hardwareMap.get(DcMotor.class, "FINT");
        shooter1 = hardwareMap.get(DcMotor.class, "S1");
        shooter2 = hardwareMap.get(DcMotor.class, "S2");
        kick = hardwareMap.get(DcMotor.class, "KICK");
        turret = hardwareMap.get(Servo.class, "TRT");;
        limelight = hardwareMap.get(Limelight3A.class, "LimeLight3A");
        imu =  hardwareMap.get(IMU.class, "imu");



    }

    @Override
    public void start() { // this runs when start button is pressed

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //waitForStart(); probably redundant now with this new start()
        runtime.reset();
        limelight.start();

    }

    @Override
    public void loop() {

        double max;

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x; //changed for specific robot was a - but now +

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;
        //double armPower = gamepad2.left_stick_y;
        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }
        //controller buttons
        boolean circle = gamepad2.circle;
        boolean triangle = gamepad2.triangle;
        boolean square = gamepad1.square; // intake toggle button
        boolean slowBtn = gamepad1.right_bumper; // slow mode toggle button

        boolean circlePressed = circle && !prevCircle; // this is for the Rising edge technique
        boolean trianglePressed = triangle && !prevTriangle;
        boolean squarePressed = square && !prevSquare;
        boolean slowPressed = slowBtn && !prevSlowMode;
//if statments
        if (circlePressed)
        {
            shooterMode = (shooterMode == ShooterMode.SOFT) ? ShooterMode.OFF : ShooterMode.SOFT;
        }
        if (trianglePressed)
        {
            shooterMode = (shooterMode == ShooterMode.HARD) ? ShooterMode.OFF : ShooterMode.HARD;
        }
// intake toggle
        if (squarePressed) {
            intakeOn = !intakeOn;
        }
        altHardware.setIntakePower(intakeOn ? 1.0 : 0.0);
        if(gamepad2.left_bumper){
            altHardware.setTurretPosition(0);
        }
        if(gamepad2.right_bumper){
            altHardware.setTurretPosition(1);
        }
        if(gamepad2.left_trigger > 0.2 && 0 <= altHardware.getTurretPosition() && altHardware.getTurretPosition() <= 1){
            altHardware.setTurretPosition(altHardware.getTurretPosition()+0.05);
            //maybe add a sleep here?
        }
        if(gamepad2.right_trigger > 0.2 &&0 <= altHardware.getTurretPosition() && altHardware.getTurretPosition() <= 1) //this might break idk
             {
            altHardware.setTurretPosition(altHardware.getTurretPosition()-0.05);
            //maybe add a sleep here?
        }

// slow mode toggle
        if (slowPressed) {
            slowModeOn = !slowModeOn;
        }

        double[] powers = {leftFrontPower, leftBackPower, rightBackPower, rightFrontPower};
        if (slowModeOn) altHardware.setMotorSlowMode(powers);
        else            altHardware.setMotorPowers(powers);

        //post if statments
        prevCircle = circle;
        prevTriangle = triangle;
        prevSquare = square;
        prevSlowMode = slowBtn;

        // Always set power every loop (include OFF case!)
        switch (shooterMode) {
            case HARD: altHardware.setShooterPower(0.6); break;
            case SOFT: altHardware.setShooterPower(0.1); break;
            default:   altHardware.setShooterPower(0.0); break;
        }







        /* Will use for limelight later

        LLResult llResult = limelight.getLatestResult();
        if (llResult != null & llResult.isValid()) // this is really important because if the limelight doesn't see anything the code WILL break down
        {
            distance = hardware.distanceCalc(llResult.getTa());
            telemetry.addData("distance: ", distance);
            telemetry.update();
            if(gamepad2.right_trigger > 0.2){
                hardware.aimbot(distance);
            }
            else{
            altHardware.setShooterPower(0);
            }
        }

        */


        telemetry.addData("Shooter", shooterMode);
        telemetry.addData("IntakeOn", intakeOn);
        telemetry.addData("SlowModeOn", slowModeOn);
        telemetry.addData("TurretPos", altHardware.getTurretPosition());
        telemetry.update();
    }

}
