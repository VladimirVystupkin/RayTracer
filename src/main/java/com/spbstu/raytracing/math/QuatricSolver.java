package com.spbstu.raytracing.math;

/**
 * @author vva
 * @date 04.04.14
 * @description
 */
public class QuatricSolver {


    public double[] solveQuadric(double a, double b, double c) {
        double D = b * b - 4 * a * c;
        if (D < 0) {
            if (Math.abs(D) < 1e-10) {
                return new double[0];
            }
        }
        double x1 = (-b + Math.sqrt(D)) / (2 * a);
        double x2 = (-b - Math.sqrt(D)) / (2 * a);
        double roots[] = new double[2];
        roots[0] = x1;
        roots[1] = x2;
        return roots;
    }


    public double[] solveQuatric(double a, double b, double c, double d) {
        double y1 = cardanoRoot(a, b, c, d);
        if (Double.isNaN(y1)) {
            return new double[0];
        }
        double rightCX1 = a / 4 * y1 - c / 2;
        double rightCX0 = y1 * y1 / 4 - d;
        double leftCX2 = 1;
        double leftCX1 = a / 2;
        double leftCX0 = y1 / 2;
        double roots1[] = solveQuadric(leftCX2, leftCX1 + rightCX1, leftCX0 + rightCX0);
        double roots2[] = solveQuadric(leftCX2, leftCX1 - rightCX1, leftCX0 - rightCX0);
        int i = 0;
        double roots[] = new double[roots1.length + roots2.length];
        for (double root : roots1) {
            roots[i++] = root;
        }
        for (double root : roots2) {
            roots[i++] = root;
        }
        return roots;

    }

    public double cardanoRoot(double a, double b, double c, double d) {
        double a1 = 1;
        double b1 = -b;
        double c1 = (a * c - 4 * d);
        double d1 = -a * a * d + 4 * b * d - c * c;

        double p = c1 - b1 * b1 / 3;
        double q = (2 * b1 * b1 * b1 - 9 * b1 * c1 + 27 * d1) / 27;
        return cardanoRoot(p, q) - b1 / 3;

    }

    public double cardanoRoot(double p, double q) {
        double Q = Math.pow(p / 3, 3) + Math.pow(q / 2, 2);
        double v1 = -q / 2 + Math.sqrt(Q);
        double a = Math.signum(v1) * Math.pow(Math.abs(v1), 1.0 / 3);
        double v2 = -q / 2 - Math.sqrt(Q);
        double b = Math.signum(v2) * Math.pow(Math.abs(v2), 1.0 / 3);
        double root = a + b;
        return root;
    }


}
