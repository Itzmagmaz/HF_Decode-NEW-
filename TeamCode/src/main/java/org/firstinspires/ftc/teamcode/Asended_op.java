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
@TeleOp (name = "Asended_op", group ="OpMode")
public class Asended_op extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor fintake = null; //Arm is a extra motor
    private DcMotor leftext = null;
    private DcMotor rightext = null;
    private DcMotor spindex = null;
    private Servo pusher = null;
    private CRServo sintake = null;

    public static final double MAX_POSITION = 6000, MIN_POSITION = 0;
    private Hardware hardware;

    private Limelight3A limelight;

    private double distance;

    boolean slowMode = false;
    //boolean armSlowMode = false;
    boolean slock = true;
    //boolean clawpos = false;
    boolean intakeToggle = false;
    double shotpower = 0.0;
    double ticks = 2786.2;
    int counter = 0;



    @Override
    public void init() {
        //GEEEEEE
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        hardware = new Hardware(hardwareMap);
        fintake = hardwareMap.get(DcMotor.class, "FINT"); //Arm is a extra motor
        leftFrontDrive = hardwareMap.get(DcMotor.class, "FL");
        leftBackDrive = hardwareMap.get(DcMotor.class, "BL");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        rightBackDrive = hardwareMap.get(DcMotor.class, "BR");
        leftext = hardwareMap.get(DcMotor.class, "LEXT");
        rightext = hardwareMap.get(DcMotor.class, "REXT");
        spindex = hardwareMap.get(DcMotor.class, "SPIN");
        pusher = hardwareMap.get(Servo.class, "PUSH");
        sintake = hardwareMap.get(CRServo.class, "SINT");
        limelight = hardwareMap.get(Limelight3A.class, "LimeLight3A");
        //claw = hardwareMap.get(Servo.class, "CLAW");


        //elbow_Left.scaleRange(0,0.25);  servo programs


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

        if (gamepad1.x) //THIS IS A SQUARE
        {
            intakeToggle = !intakeToggle;
            hardware.ezzysleep(25);

        }
        if(intakeToggle){
            hardware.setFintakePower(1);
            hardware.setSintakePower(1);
        }
        else{
            hardware.setFintakePower(0);
            hardware.setSintakePower(0);
        }

        if (gamepad1.dpad_left){
            hardware.turnLeft(45,1);
        }
        else if (gamepad1.dpad_right){
            hardware.turnRight(45,1);
        }

        if (gamepad1.left_bumper) {
            slowMode = !slowMode;
            hardware.ezzysleep(25);
        }

        if(gamepad2.dpad_up){
            shotpower = hardware.dpadsleepP(shotpower);
        }
        if(gamepad2.dpad_down){
            shotpower =hardware.dpadsleepM(shotpower);
        }
        hardware.setLeftextPower(shotpower);
        hardware.setRightextPower(shotpower);

        if(gamepad2.triangle){
            hardware.setPushposition(1);
            hardware.ezzysleep(100);
            hardware.setPushposition(0);
            counter++;
        }
        if(gamepad2.circle){
            hardware.setSpindexposition((int)ticks);
            hardware.ezzysleep(60);
        }


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
                hardware.setLeftextPower(0);
                hardware.setRightextPower(0);
            }
        }

        if(gamepad2.right_trigger > 0.2){
            hardware.setSpindexpower(gamepad2.right_trigger/4);
        }
        if(gamepad2.left_trigger > 0.2){
            hardware.setSpindexpower(-(gamepad2.left_trigger/4));
        }
        else{
            hardware.setSpindexpower(0);
        }




    }


}


