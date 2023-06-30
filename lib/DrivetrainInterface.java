public interface DrivetrainInterface{
    public enum DriveMode{POWER, VELOCITY, DISTANCE, ODOMETRY};

    public void drive(double drive, double strafe, double turn, double value);

    public void setMode(DRIVE_MODE drivemode)
}
