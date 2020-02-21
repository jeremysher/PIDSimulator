package io.github.jeremysher.pid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import io.github.jeremysher.animation.Animation;

public class DriveDistance {

	public static void main(String[] args) {
		Animation a = new Animation(1000, 500, "PID Simulator") {
			
			private final double wheelY = 250;
			
			private final double kS = 1;
			private final double kV = 3;
			private final double kA = 0.5;
			
			private double scale = 100; // pixels per meter
			private double setpointX = 9; // meters
			private double wheelX = 0.5; // m
			private double radius = 0.1; // m
			private double mass = 1.0; //kg
			private double omega = 0.0; // rad/s
			private double minOmega = 1; // rad/s (min angular velocity to move
			
			private PIDController pidController = new PIDController(1000, 0, 100,
					() -> getDeltaTime(),
					() -> wheelX,
					() -> setpointX
			);
			

			@Override
			public void draw(Graphics2D g) {
				g.setColor(Color.blue);
				g.fillOval((int)(wheelX * scale - radius * scale), (int)wheelY, (int)(2 * radius * scale), (int)(2 * radius * scale));
				
				g.setColor(Color.red);
				g.drawRect((int)(setpointX * scale), 0, 0, 500);
				
			}

			@Override
			public void run() {
				double dt = getDeltaTime();
				
				double voltage = pidController.calculate();
				
				double alpha = (Math.abs(voltage - kV * omega) > kS ? (voltage - kV * omega - kS * Math.signum(omega != 0 ? omega : voltage)) / kA : 0);
				omega += alpha * dt;
				omega *= 0.95;
				double v = (Math.abs(omega) > minOmega || Math.abs(voltage) > kS ? omega * radius : 0);
				wheelX += v * dt;
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				setpointX = e.getX() / scale;
			}
			
		};
		
		a.start();
	}

}
