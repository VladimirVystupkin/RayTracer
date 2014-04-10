package com.spbstu.raytracing.math;

/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class PointExt extends Point3D{
    Object info;
    public PointExt(double x, double y, double z, Object info) {
        super(x, y, z);
        this.info =info;
    }

    public PointExt(Vector vector, Object info) {
        super(vector);
        this.info =info;
    }
    public PointExt(Point3D point, Object info) {
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
