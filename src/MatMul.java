import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;


public class MatMul {

    public static double[][] readMatrix(Scanner scanner) {
        int nRows = scanner.nextInt(), nCols = scanner.nextInt();
        double result[][] = new double[nRows][];

        for(int row = 0; row < nRows; row++) {
            result[row] = new double[nCols];
            for(int col = 0; col < nCols; col++) {
                result[row][col] = scanner.nextDouble();
            }
        }

        return result;
    }

    public static double[][] computeMatMul(double[][] mat1, double[][] mat2) {
        assert mat1.length > 0;
        assert mat2.length > 0;

        int dim1 = mat1.length, innerDim = mat1[0].length, dim2 = mat2[0].length;
        assert innerDim == mat2.length;

        double result[][] = new double[dim1][];
        for(int row = 0; row < dim1; row++) {
            result[row] = new double[dim2];
            for(int col = 0; col < dim2; col++) {
                double cell = 0;
                for(int i = 0; i < innerDim; i++) {
                    cell += mat1[row][i] * mat2[i][col];
                }
                result[row][col] = cell;
            }
        }
        return result;
    }

    public static int main(String argv[]) {
        double mat1[][], mat2[][];

        try {
            if (argv.length == 2) {
                try {
                    mat1 = readMatrix(new Scanner(new File(argv[0])));
                    mat2 = readMatrix(new Scanner(new File(argv[1])));
                } catch (FileNotFoundException exc) {
                    System.err.println("Can not load matrix: " + exc.getMessage());
                    return 1;
                }
            } else if (argv.length == 0) {
                Scanner scanner = new Scanner(System.in);
                mat1 = readMatrix(scanner);
                mat2 = readMatrix(scanner);
            } else {
                System.err.println("Usage: no args (read from stdin) or 2 args (file names)");
                return 1;
            }
        } catch (InputMismatchException exc) {
            System.err.println("Malformed matrix");
            return 1;
        } catch (NoSuchElementException exc) {
            System.err.println("Unexpected end of file");
            return 1;
        }

        double result[][] = computeMatMul(mat1, mat2);

        for(double row[] : result) {
            for(double item : row) {
                System.out.print(item);
                System.out.print(" ");
            }
            System.out.println();
        }
        return 0;
    }

}
