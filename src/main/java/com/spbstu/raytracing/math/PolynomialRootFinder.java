package com.spbstu.raytracing.math;


import org.ejml.alg.dense.decomposition.DecompositionFactory;
import org.ejml.alg.dense.decomposition.EigenDecomposition;
import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor vystupkin
 * @date 01.04.14
 * @description
 */
public class PolynomialRootFinder {

    /**
     * <p>
     * Given a set of polynomial coefficients, compute the roots of the polynomial.  Depending on
     * the polynomial being considered the roots may contain complex number.  When complex numbers are
     * present they will come in pairs of complex conjugates.
     * </p>
     *
     * @param coefficients Coefficients of the polynomial.
     * @return The roots of the polynomial
     */
    public static Complex64F[] findRoots(double... coefficients) {
        int N = coefficients.length - 1;

        // Construct the companion matrix
        DenseMatrix64F c = new DenseMatrix64F(N, N);

        double a = coefficients[N];
        for (int i = 0; i < N; i++) {
            c.set(i, N - 1, -coefficients[i] / a);
        }
        for (int i = 1; i < N; i++) {
            c.set(i, i - 1, 1);
        }

        // use generalized eigenvalue decomposition to find the roots

        EigenDecomposition<DenseMatrix64F> evd = DecompositionFactory.eigGeneral(N, false);

        evd.decompose(c);

        Complex64F[] roots = new Complex64F[N];

        for (int i = 0; i < N; i++) {
            roots[i] = evd.getEigenvalue(i);
        }

        return roots;
    }

    public static double[] getRoots(double... coefficients) {
        List<Double> realRoots = new ArrayList<>();
        for (Complex64F root : findRoots(coefficients)) {
            if (root.isReal()) {
                realRoots.add(root.getReal());
            }
        }
        double roots[] = new double[realRoots.size()];
        for (int i = 0; i < roots.length; i++) {
            roots[i] = realRoots.get(i);
        }
        return roots;
    }
}
