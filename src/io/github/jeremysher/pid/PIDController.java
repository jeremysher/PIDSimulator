package io.github.jeremysher.pid;

import java.util.function.DoubleSupplier;

public class PIDController {
	private final double kP, kI, kD;
	private final DoubleSupplier dtSupplier, measurementSupplier, setpointSupplier;
	private double lastError = 0.0;
	private double sum = 0.0;
	private double pTolerance, vTolerance = 0.0;
	private double error, errorRate;
	
	public PIDController(double p, double i, double d,
			DoubleSupplier dt, DoubleSupplier measurement, DoubleSupplier setpoint) {
		kP = p;
		kI = i;
		kD = d;
		dtSupplier = dt;
		measurementSupplier = measurement;
		setpointSupplier = setpoint;
	}
	
	public double calculate() {
		double dt = dtSupplier.getAsDouble();
		double measurement = measurementSupplier.getAsDouble();
		double setpoint = setpointSupplier.getAsDouble();
		
		error = setpoint - measurement;
		sum += error * dt;
		errorRate = (error - lastError) / dt;
		lastError = error;
		
		return kP * error + kI * sum + kD * errorRate;
	}
	
	public boolean atSetpoint() {
		return Math.abs(error) < pTolerance && Math.abs(errorRate) < vTolerance;
	}
	
	public void setTolerance(double positionTolerance) {
		setTolerance(positionTolerance, 0);
	}
	
	public void setTolerance(double positionTolerance, double velocityTolerance) {
		pTolerance = positionTolerance;
		vTolerance = velocityTolerance;
	}

}
