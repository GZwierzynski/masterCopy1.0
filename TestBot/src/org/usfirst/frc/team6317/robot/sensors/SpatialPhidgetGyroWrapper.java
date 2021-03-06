package org.usfirst.frc.team6317.robot.sensors;

import com.phidgets.PhidgetException;
import com.phidgets.SpatialEventData;
import com.phidgets.SpatialPhidget;
import com.phidgets.event.SpatialDataEvent;
import com.phidgets.event.SpatialDataListener;

public class SpatialPhidgetGyroWrapper implements SpatialDataListener 
{
	private final SpatialPhidget sp;
	private final int gyroAxes;
	private final double[] integratedGyroAxes;
	private double lastTime = 0;
	
	public SpatialPhidgetGyroWrapper(SpatialPhidget spatialPhidget) throws PhidgetException 
	{
		this.sp = spatialPhidget;
		this.sp.addSpatialDataListener(this);
		this.gyroAxes = sp.getGyroAxisCount();
		this.integratedGyroAxes = new double[gyroAxes];
	}
	
	public double getIntegratedAxis(int axis) {
		return this.integratedGyroAxes[axis];
	}
	
	public double getIntegratedXAxis() {
		return this.getIntegratedAxis(0);
	}
	
	public double getIntegratedYAxis() {
		return this.getIntegratedAxis(1);
	}
	
	public double getIntegratedZAxis() {
		return this.getIntegratedAxis(2);
	}
	
	public double getHeading(int axis) {
		double heading = this.integratedGyroAxes[axis] % 360;
		if (heading < 0) heading += 360;
		return heading;
	}
	
	public double getXHeading() {
		return this.getHeading(0);
	}
	
	public double getYHeading() {
		return this.getHeading(1);
	}
	
	public double getZHeading() {
		return this.getHeading(2);
	}

	@Override
	public void data(SpatialDataEvent e) {
		SpatialEventData data = e.getData()[0];
		double time = data.getTime();
		if (this.lastTime == 0) {
			this.lastTime = time;
			return;
		}
		for (int i = 0; i < this.gyroAxes; ++i) {
			this.integratedGyroAxes[i] += data.getAngularRate()[i] * (time - this.lastTime);
		}
		this.lastTime = time;
	}
}
