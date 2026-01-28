package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RESET_ENCODERS;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


public class AltHardware {

    //This Hardware class was made of the intent that Calvin and Alec made on 1/24/25


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

    public static final double SLOW_RATE = 0.3;

    //need to add Odementry here

    private Limelight3A limelight;
    public AltHardware(HardwareMap hardwareMap) {

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

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        shooter1.setDirection(DcMotor.Direction.FORWARD);
        shooter2.setDirection(DcMotor.Direction.FORWARD);
        kick.setDirection(DcMotor.Direction.FORWARD);
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP; //
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        // Now initialize the IMU with this mounting orientation
        // Note: if you choose two conflicting directions, this initialization will cause a code exception.
        imu.initialize(new IMU.Parameters(orientationOnRobot));


    }
    public void setMotorPowers(double... powers)
    {
        leftFrontDrive.setPower(powers[0]);
        leftBackDrive.setPower(powers[1]);
        rightBackDrive.setPower(powers[2]);
        rightFrontDrive.setPower(powers[3]);
    }
    public void setMotorSlowMode(double... powers)
    {
        leftFrontDrive.setPower(powers[0] * SLOW_RATE);
        leftBackDrive.setPower(powers[1] * SLOW_RATE);
        rightBackDrive.setPower(powers[2] * SLOW_RATE);
        rightFrontDrive.setPower(powers[3] * SLOW_RATE);
        try {
            Thread.sleep(75);
        } catch (InterruptedException e) {}

    }
    public void setMotorPower(double fl, double fr, double bl, double br)
    {
        leftFrontDrive.setPower(fl);
        rightFrontDrive.setPower(fr);
        leftBackDrive.setPower(bl);
        rightBackDrive.setPower(br);
    }

    public void setMotorTargets(double fl, double fr, double bl, double br)
    {
        leftFrontDrive.setTargetPosition((int) fl);
        rightFrontDrive.setTargetPosition((int) fr);
        leftBackDrive.setTargetPosition((int) bl);
        rightBackDrive.setTargetPosition((int) br);
    }

    public void setMotorModes(DcMotor.RunMode mode) {
        leftFrontDrive.setMode(mode);
        rightFrontDrive.setMode(mode);
        leftBackDrive.setMode(mode);
        rightBackDrive.setMode(mode);
    }

    //collection of random motors and servos SETTERS

    public void setShooterPower(double power){
        shooter1.setPower(power);
        shooter2.setPower(power);
    }
    public void setIntakePower(double power){
        intake.setPower(power);
    }
    public void setKickPower(double power){
        kick.setPower(power);
    }

    public void setTurretPosition(double power){
        turret.setPosition(power);
    }

    //collection of random getters
    public double getShooterPower(){
        return shooter1.getPower();
    }
    public double getIntakePower(){
        return intake.getPower();
    }
    public double getKickPower(){
        return kick.getPower();
    }
    public double getTurretPosition(){
        return turret.getPosition();
    }

    public double getYaw() { // some imu function
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

        return orientation.getYaw(AngleUnit.DEGREES);
    }
    public void ezzysleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }


}
