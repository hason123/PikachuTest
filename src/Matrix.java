import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Matrix {
    private int row;
    private int col;
    private int[][] matrix;
    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;
        System.out.println(row + "," + col);
        createMatrix();
        showMatrix();
    }
    public void showMatrix() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }
    }
    public void createMatrix() {
        matrix = new int[row][col];
        Random rand = new Random();
        int imgCount = 35; // so anh trong folder res
        int max = 6; //so lan icon xuat hien toi da la 6
        int arr[] = new int[imgCount + 1];
        ArrayList<Point> listPoint = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                listPoint.add(new Point(i, j));
            }
        }
        int i = 0;
        do {
            int index = rand.nextInt(imgCount) + 1;
            if (arr[index] < max) {
                arr[index] += 2;
                for (int j = 0; j < 2; j++) {
                    int size = listPoint.size();
                    int pointIndex = rand.nextInt(size);
                    matrix[listPoint.get(pointIndex).x]
                            [listPoint.get(pointIndex).y] = index;
                    listPoint.remove(pointIndex);
                }
                i++;
            }
        } while (i < row * col / 2);
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }
}