package org.firstinspires.ftc.teamcode;/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Goat_op", group="Linear OpMode")
public class  Goat_op extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor arm = null; //Arm is a extra motor

    //private Servo claw;

    public static final double MAX_POSITION = 6000, MIN_POSITION = 0;
    private Hardware hardware;

    //boolean slowMode = false;
    //boolean armSlowMode = false;


    @Override
    public void runOpMode() {
        //GEEEEEE
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        hardware = new Hardware(hardwareMap);
        arm = hardwareMap.get(DcMotor.class, "ARM"); //Arm is a extra motor
        slider = hardwareMap.get(DcMotor.class, "SLIDE");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "FL");
        leftBackDrive = hardwareMap.get(DcMotor.class, "BL");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        rightBackDrive = hardwareMap.get(DcMotor.class, "BR");
        claw = hardwareMap.get(Servo.class, "CLAW");
        pusher = hardwareMap.get(Servo.class, "PUSH");
        wrist  = hardwareMap.get(Servo.class, "WRIST");
        bucket = hardwareMap.get(Servo.class, "BUCK");


        claw.scaleRange(0.4,1);
        //claw_Green.scaleRange(0.25, 0.75);
        //elbow_Left.scaleRange(0,0.25);  servo programs

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        boolean slowMode = false;
        boolean armSlowMode = false;
        boolean slock = true;
        boolean clawpos = false;

        String clawPosUpdater = "" + claw.getPosition();

        while (opModeIsActive()) {


            // run until the end of the match (driver presses STOP)
            //while (opModeIsActive()) {

            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = -gamepad1.right_stick_x;
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;
            double armPower = gamepad2.left_stick_y;
            double slidePower = gamepad2.right_stick_y;


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

            if (gamepad1.left_bumper)
                slowMode = !slowMode;
            if (gamepad2.y)
                armSlowMode = !armSlowMode;
            if (gamepad2.circle) { //b WORKING GOOD (WRIST)
                if (pushpos == false){
                    pusher.setPosition(0.5);
                    sleep(200);
                    pushpos = true;}
                else if (pushpos == true){
                    pusher.setPosition(0.1); //close
                    sleep(200);
                    pushpos = false;
                }
            }
            if (gamepad2.square) { //x
                if (clawpos == false) {
                    claw.setPosition(1.0);
                    clawpos = true;
                }
                else if (clawpos == true) { //open
                    claw.setPosition(0.0);
                    clawpos = false;
                }
            }
            if (gamepad2.triangle) {
                if (bucket.getPosition() <= 0.5)
                    bucket.setPosition(1);
                if (bucket.getPosition() >= 0.5)
                    bucket.setPosition(0); //close
            }
            if (gamepad2.cross) {
                if (wristpos == false){
                    wrist.setPosition(1);
                    sleep(200);
                    wristpos = true;}

                else if (wristpos == true) {
                    wrist.setPosition(0); //close
                    sleep(200);
                    wristpos = false;
                }
            }






            // Send calculated power to wheels
            double []powers = {leftFrontPower, leftBackPower, rightBackPower, rightFrontPower};
            if (slowMode)
                hardware.setMotorSlowMode(powers);
            else
                hardware.setMotorPowers(powers);

            if (arm.getPower() > 0 && arm.getCurrentPosition() > MAX_POSITION) {
                arm.setPower(0);
            } else if (arm.getPower() < 0 && arm.getCurrentPosition() < MIN_POSITION) {
                arm.setPower(0);
            }

            if (arm.getPower() < 0.1 || arm.getPower() > -0.1 && slock == true){
                arm.setTargetPosition(arm.getCurrentPosition());
            }

            if (slider.getPower() > 0 && slider.getCurrentPosition() > MAX_POSITION) {
                slider.setPower(0);
            } else if (slider.getPower() < 0 && slider.getCurrentPosition() < MIN_POSITION) {
                slider.setPower(0);
            }

            if (slider.getPower() < 0.1 || slider.getPower() > -0.1){
                slider.setTargetPosition(slider.getCurrentPosition());
            }

            if (gamepad1.dpad_left){
                hardware.turnLeft(45,1);
            }
            else if (gamepad1.dpad_right){
                hardware.turnRight(45,1);
            }

           /* if (armSlowMode)
                hardware.setArmsSlowMode(armPower);
            else
                hardware.setArmPower(armPower);


            if (gamepad2.left_bumper && slock == false)
                slock = true;
            else if (gamepad2.left_bumper && slock == true) {
                slock = false;
                sleep(1000);
                slock = true;
            }



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("jit ", claw.getPosition());
            telemetry.update();
        }
    }
}

*/

