package com.spbstu.raytracing.math;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;

/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class Point3DExt extends Point3D{
    Object info;
    public Point3DExt(double x, double y, double z, Object info) {
        super(x, y, z);
        this.info =info;
    }

    public Point3DExt(Vector3D vector, Object info) {
        super(vector);
        this.info =info;
    }
    public Point3DExt(Point3D point, Object info) {
        super(point.x,point.y, point.z);
        this.info =info;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info){
        this.info =info;
    }
}
