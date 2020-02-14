package io.github.jeremysher.pid;

import java.util.function.DoubleSupplier;

public class PIDController {
	private final double kP, kI, kD, kIz, kF;
	private final DoubleSupplier dtSupplier, measurementSupplier, setpointSupplier;
	private double lastError = 0.0;
	private double sum = 0.0;
	
	public PIDController(double p, double i, double d, double iz, double ff,
			DoubleSupplier dt, DoubleSupplier measurement, DoubleSupplier setpoint) {
		kP = p;
		kI = i;
		kD = d;
		kIz = iz;
		kF = ff;
		dtSupplier = dt;
		measurementSupplier = measurement;
		setpointSupplier = setpoint;
	}
	
	public double calculate() {
		double dt = dtSupplier.getAsDouble();
		double measurement = measurementSupplier.getAsDouble();
		double setpoint = setpointSupplier.getAsDouble();
		
		double error = setpoint - measurement;
		sum += error * dt;
		double errorRate = (error - lastError) / dt;
		lastError = error;
		
		return kP * error + kI * sum + kD * errorRate;
	}

}
