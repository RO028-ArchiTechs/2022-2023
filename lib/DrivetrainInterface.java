public interface DrivetrainInterface{
    private enum DRIVE_MODE{POWER, VELOCITY, DISTANCE, ODOMETRY};
    public void Drive(double drive, double strafe, double turn, double value);
    public void setDriveMode(DRIVE_MODE drivewith);
    public void setStopMode(DRIVE_MODE stopwith);
    public void setMode(DRIVE_MODE drivemode)
}
