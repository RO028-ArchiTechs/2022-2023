package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class OdometricCalculator
{
    double oldX = 0, oldY = 0, theta, r, d, currentX, currentY;
    public OdometricCalculator(){
        r = 0;
        d = 0;
    }
    public OdometricCalculator(double r, double d){
        this.r = r;
        this.d = d;
    }
    // r -> radius, distance from the center of the robot to the side wheels
    // d -> distance (or something like that), distance from the center of the robot to the back wheel
    // L, R, B -> amount that the left, right and respectively back wheel rotate (up, right and left, i think, are positive)
    public Map<String, Double> getDifference(double R, double L, double B){
        Map<String, Double> coordinates = new HashMap<>();
        double dTheta = (R-L)/2*r;
        double dYrel = B-d*dTheta;
        double dXrel = R+L/2;
        dXrel = dXrel*cos(dTheta/2) - dYrel*sin(dTheta/2);
        dYrel = dYrel*cos(dTheta/2) + dXrel*sin(dTheta/2);
        coordinates.put("dXrel", dXrel);
        coordinates.put("dYrel", dYrel);
        coordinates.put("dTheta", dTheta);
        return coordinates;
    }
    // use getDifference whenever you just want to figure out only the exact amount the robot's coordinates have changed
    // it's basically considering a situation in which the robot just got started, currentX, currentY and theta are 0 (and therefore have
    // essentially no effect on the equations)
    public void updatePosition(double R, double L, double B){
        double dTheta = (R-L)/2*r;
        double dYrel = B-d*dTheta;
        double dXrel = R+L/2;
        currentX = this.oldX + dXrel*cos(theta+dTheta/2) - dYrel*sin(Theta+dTheta/2);
        currentY = this.oldY + dYrel*cos(theta+dTheta/2) + dXrel*sin(theta+dTheta/2);
        theta = theta + dTheta;
        oldX = currentX;
        oldY = currentY;
    }
    // call this function as many times throughout the code as you can,
    // !!! ESPECIALLY IF YOU HAVE A SERIES OF INSTRUCTIONS BEING EXECUTED, CALL IT AFTER EACH INDIVIDUAL INSTRUCTION !!!

    //getters and setters
    public double getX(){
        return currentX;
    }
    public double getY(){
        return currentY;
    }
    public double getTheta(){
        return theta;
    }
    public Map<String, Double> getCoords(){
        coordinates.put("X", currentX);
        coordinates.put("Y", currentY);
        coordinates.put("theta", theta);
    }
    public void setX(double currentX){
        currentX = X;
    }
    public void setY(double currentY){
        this.currentY = currentY;
    }
    public void setTheta(double theta){
        this.theta = theta;
    }
    public void setCoords(Map<String, Double> coordinates){
        currentX = coordinates.X;
        currentY = coordinates.Y;
        theta = coordinates.theta;
    }
}