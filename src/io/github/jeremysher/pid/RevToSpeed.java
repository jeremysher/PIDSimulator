package io.github.jeremysher.pid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import io.github.jeremysher.animation.Animation;

public class RevToSpeed {

	public static void main(String[] args) {
		Animation a = new Animation(1000, 500, "RevToSpeed") {

			private final double wheelY = 250;
			
			private final double kS = 0.171;
			private final double kV = 0.129;
			private final double kA = 0.018;
			
			private double scale = 1000; // pixels per meter
			private double setSpeed = 240; // rad/s
			private double wheelX = 0.5; // m
			private double radius = 0.1; // m
			private double mass = 1.0; //kg
			private double omega = 0.0; // rad/s
			private double minOmega = 1; // rad/s (min angular velocity to move
			private double theta = 0; //rad
			private double maxAcc = 666; //rad/s^2
			
			private PIDController pidController = new PIDController(1.977, 0, 0,
					() -> getDeltaTime(),
					() -> omega,
					() -> setSpeed
			);
			

			@Override
			public void draw(Graphics2D g) {
				g.setColor(Color.blue);
				g.fillOval((int)(wheelX * scale - radius * scale), (int)wheelY, (int)(2 * radius * scale), (int)(2 * radius * scale));
				
				g.setColor(Color.red);
				g.fillOval((int)((wheelX * scale) + Math.cos(theta) * radius * scale - 5),
						(int)(wheelY + radius * scale + Math.sin(theta) * radius * scale - 5),
						10,
						10);
				
			}

			@Override
			public void run() {
				double dt = getDeltaTime();
				
				double voltage = pidController.calculate();
				
				double alpha = (Math.abs(voltage - kV * omega) > kS ? (voltage - kV * omega - kS * Math.signum(omega != 0 ? omega : voltage)) / kA : 0);
				alpha = Math.min(alpha, maxAcc);
				omega += alpha * dt;
				//omega *= 0.95;
				omega = (Math.abs(omega) > minOmega || Math.abs(voltage) > kS ? omega : 0);
				theta += omega * dt;
				System.out.println(omega);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				omega -= 100;
			}
			
		};
		a.start();

	}
}
