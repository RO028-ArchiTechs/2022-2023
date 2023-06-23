package org.firstinspires.ftc.teamcode.lib;

public interface DrivetrainInterface
{
    // main methods
    public void DriveWithPower();
    public void DriveWithVelocity();
    public void DriveWithDistance();
    public void DriveWithOdometry();
    
    public void DriveStop();
    public void DriveStopPower();

    // codes (for ease of use), to be overridden with
    // a call to a main method, as listed below
    public void DWP(); // DriveWithDistance();
    public void DWV(); // DriveWithVelocity();
    public void DWD(); // DriveWithDistance();
    public void DWO(); // DriveWithOdometry();

    public void DST(); // DriveStop();
    public void DSP(); // DriveStopPower();
}

