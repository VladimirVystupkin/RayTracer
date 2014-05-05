package com.spbstu.raytracing.math;



/**
 * A {@link com.spbstu.raytracing.math.Point} extension to keep inner info
 *
 * @author vva
 * @see com.spbstu.raytracing.math.Point
 */
public class PointExt extends Point {
    final Object info;

    /**
     * Point extension constructor by 3 coordinates and inner info
     *
     * @param x    x coordinate
     * @param y    y coordinate
     * @param z    z coordinate
     * @param info inner info
     */
    public PointExt(final double x, final double y, final double z, final Object info) {
        super(x, y, z);
        this.info = info;
    }


    /**
     * Point extension constructor from {@link com.spbstu.raytracing.math.Point}  with inner info
     *
     * @param point {@link com.spbstu.raytracing.math.Point} for conversation
     * @param info  inner info
     */
    public PointExt(final Point point, final Object info) {
        super(point.x, point.y, point.z);
        this.info = info;
    }

    /**
     * Returns inner info
     *
     * @return inner info
     */

    public Object getInfo() {
        return info;
    }

}
