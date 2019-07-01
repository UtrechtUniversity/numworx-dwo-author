/*
 * File:    Matrix.java
 *
 * Provides basic linear algebra matrix functionality (e.g. transpose, invert, multiply)
 */

package fi.ivmdrawgwt.client;


import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides very ad-hoc Linear Algebra implementation of a matrix represented by a 2d Double array.
 */
public class Matrix {
    private double[][] values;
    private int xDimension;
    private int yDimension;

    public double[] xValues() {
        return this.transpose().values[0];
    }


    public double[] yValues() {
        return this.transpose().values[1];
    }


    public double[][] values() {
        return this.values;
    }


    public int xLength() {
        return this.xDimension;
    }


    public int yLength() {
        return this.yDimension;
    }

    /**
     * Constructor for a dataset represented by a n X 2 matrix.
     * @param xs
     * @param ys
     */
    public Matrix(double[] xs, double[] ys) {
        this.values = new double[ys.length][2];
        this.xDimension = 2;
        this.yDimension = ys.length;

        for (int i = 0; i < this.yDimension; i++) {
            values[i][0] = xs[i];
            values[i][1] = ys[i];
        }
    }


    /**
     * Contstructor for a 2D double array of size m X n.
     * @param newValues
     */
    public Matrix(double[][] newValues) {
        this.xDimension = newValues[0].length;
        this.yDimension = newValues.length;

        this.values = newValues;
    }


    /**
     * Return the transpose of this matrix.
     * @return
     */
    public Matrix transpose() {
        double[][] newValues = new double[this.xDimension][this.yDimension];

        for (int i = 0; i < this.yDimension; i++) {
            for (int j = 0; j < this.xDimension; j++) {
                newValues[j][i] = this.values[i][j];
            }
        }

        return new Matrix(newValues);
    }


    /**
     * Multiply this matrix with a given matrix.
     * @param b Matrix
     * @return
     */
    public Matrix dot(Matrix b) {
        double[][] newValues = new double[this.yDimension][b.xDimension];

        for (int i = 0; i < this.yDimension; i++) {
            for (int j = 0; j < b.xLength(); j++) {
                for (int z = 0; z < this.xDimension; z++) {
                    newValues[i][j] += this.values[i][z] * b.values()[z][j];
                }
            }
        }

        return new Matrix(newValues);
    }


    /**
     * Return the innverse of this matrix.
     * Source: https://www.sanfoundry.com/java-program-find-inverse-matrix/
     * @return
     */
    public Matrix invert() {
        int n = this.xDimension;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];

        for (int i=0; i<n; ++i) {
            b[i][i] = 1;
        }

        // Transform the matrix into an upper triangle
        gaussian(this.values(), index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i) {
            for (int j=i+1; j<n; ++j) {
                for (int k=0; k<n; ++k) {
                    b[index[j]][k] -= this.values[index[j]][i]*b[index[i]][k];
                }
            }
        }

        // Perform backward substitutions
        for (int i=0; i<n; ++i) {
            x[n-1][i] = b[index[n-1]][i]/this.values[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) {
                x[j][i] = b[index[j]][i];

                for (int k=j+1; k<n; ++k) {
                    x[j][i] -= this.values[index[j]][k]*x[k][i];
                }

                x[j][i] /= this.values[index[j]][j];
            }
        }

        return new Matrix(x);
    }


    /**
     * Source: https://www.sanfoundry.com/java-program-find-inverse-matrix/
     * @param a
     * @param index
     */
    private static void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i) {
            index[i] = i;
        }

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) {
            double c1 = 0;

            for (int j=0; j<n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }

            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j) {
            double pi1 = 0;

            for (int i=j; i<n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];

                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;

            for (int i=j+1; i<n; ++i) {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l) {
                    a[index[i]][l] -= pj*a[index[j]][l];
                }
            }
        }
    }


    @Override
    public String toString() {
        return Arrays.deepToString(this.values) + "\n";
    }
}
