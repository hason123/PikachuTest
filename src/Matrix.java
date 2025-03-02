import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Matrix {
    private int row;
    private int col;
    private int[][] matrix;
    private ArrayList<Point> paths = new ArrayList<Point>();
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
        for (int i = 0; i < col; i++) {
            matrix[0][i] = matrix[row - 1][i] = 0;
        }
        for (int i = 0; i < row; i++) {
            matrix[i][0] = matrix[i][col - 1] = 0;
        }

        Random rand = new Random();
        int imgCount = 10; //
        int max = 20;       // số lần icon xuất hiện tối đa
        int[] arr = new int[imgCount + 1]; //from arr[0] to arr[20]

        ArrayList<Point> listPoint = new ArrayList<>();

        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                listPoint.add(new Point(i, j));
            }
        }

        int i = 0;
        while ( i < (row-2) * (col-2) / 2) {
            int index = rand.nextInt(imgCount) + 1; //from 1 to 20
            if (arr[index] < max) {
                arr[index] += 2;
                for (int j = 0; j < 2 ; j++) {
                    int size = listPoint.size();
                    int pointIndex = rand.nextInt(size);
                    Point point = listPoint.get(pointIndex);
                    matrix[point.x][point.y] = index;
                    listPoint.remove(pointIndex);
                }
                i++;
            }
        }
    }
    public void shiftMatrix() {
        for (int i = 1; i < row - 1; i++) { // Bỏ qua viền ngoài
            int[] newRow = new int[col];
            int index = 1; // Bắt đầu từ cột 1 (bỏ qua viền ngoài)
            // Duyệt qua hàng hiện tại
            for (int j = 1; j < col - 1; j++) {
                if (matrix[i][j] != 0) {
                    newRow[index] = matrix[i][j];
                    index++;// Gán giá trị không phải 0 vào vị trí mới
                }
            }
            // Cập nhật lại hàng trong ma trận
            for (int j = 1; j < col - 1; j++) {
                matrix[i][j] = newRow[j];
            }
        }
    }

    public void shiftMatrix3() {
        for (int i = 1; i < row - 1; i++) { // Bỏ qua viền ngoài
            int[] newRow = new int[col];
            int index = col - 2 ; // Bắt đầu từ cột 1 (bỏ qua viền ngoài)
            // Duyệt qua hàng hiện tại
            for (int j = col - 2; j >= 1; j--) {
                if (matrix[i][j] != 0) {
                    newRow[index] = matrix[i][j];
                    index--;// Gán giá trị không phải 0 vào vị trí mới
                }
            }
            // Cập nhật lại hàng trong ma trận
            for (int j = 1; j < col - 1; j++) {
                matrix[i][j] = newRow[j];
            }
        }
    }

    public void shiftMatrix4() {
        for (int j = 1; j < col - 1; j++) { // Duyệt qua từng cột (bỏ qua viền ngoài)
            int[] newCol = new int[row]; // Tạo một mảng mới cho cột
            int index = 1; // Bắt đầu từ hàng cuối cùng (bỏ qua viền ngoài)
            // Duyệt qua các hàng trong cột
            for (int i = 1; i < row - 1 ; i++) {
                if (matrix[i][j] != 0) {
                    newCol[index] = matrix[i][j];
                    index++;// Gán giá trị không phải 0 vào vị trí mới trong cột
                }
            }
            // Cập nhật lại cột trong ma trận
            for (int i = 1; i < row - 1; i++) {
                matrix[i][j] = newCol[i];
            }
        }
    }

    public void shiftMatrix2() {
        for (int j = 1; j < col - 1; j++) { // Duyệt qua từng cột (bỏ qua viền ngoài)
            int[] newCol = new int[row]; // Tạo một mảng mới cho cột
            int index = row - 2; // Bắt đầu từ hàng cuối cùng (bỏ qua viền ngoài)
            // Duyệt qua các hàng trong cột
            for (int i = row - 2; i >= 1; i--) {
                if (matrix[i][j] != 0) {
                    newCol[index] = matrix[i][j];
                    index--;// Gán giá trị không phải 0 vào vị trí mới trong cột
                }
            }
            // Cập nhật lại cột trong ma trận
            for (int i = 1; i < row - 1; i++) {
                matrix[i][j] = newCol[i];
            }
        }
    }

    public void shuffleMatrix() {
        // Tạo danh sách các vị trí có giá trị khác 0
        ArrayList<Point> nonZeroPoints = new ArrayList<>();
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                if (matrix[i][j] != 0) {
                    nonZeroPoints.add(new Point(i, j));
                }
            }
        }

        Random rand = new Random();
        // Shuffle ngẫu nhiên danh sách các điểm có giá trị khác 0
        Collections.shuffle(nonZeroPoints);

        // Sau khi shuffle, tiến hành hoán đổi các vị trí trong ma trận
        for (int i = 0; i < nonZeroPoints.size(); i++) {
            Point p1 = nonZeroPoints.get(i); //
            int value1 = matrix[p1.x][p1.y];

            // Chọn một vị trí khác ngẫu nhiên từ danh sách đã shuffle
            int randomIndex = rand.nextInt(nonZeroPoints.size());
            Point p2 = nonZeroPoints.get(randomIndex);
            int value2 = matrix[p2.x][p2.y];

            // Hoán đổi giá trị giữa hai vị trí
            matrix[p1.x][p1.y] = value2;
            matrix[p2.x][p2.y] = value1;
        }

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

    public ArrayList<Point> getPaths() {
        return paths;
    }


    /*Giai thuat kiem tra 2 diem da click vao co duong noi voi nhau hay khong */
    /*TH1: Cung nam tren 1 hang hoac 1 cot*/
    private boolean checkLineX(int y1, int y2, int x) {
        int minCol = Math.min(y1, y2);
        int maxCol = Math.max(y1, y2);
        for (int y = minCol + 1; y < maxCol; y++) {
            if (matrix[x][y] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLineY(int x1, int x2, int y) {
        int maxRow = Math.max(x1, x2);
        int minRow = Math.min(x1, x2);
        for (int x = minRow + 1; x < maxRow; x++) {
            if (this.matrix[x][y] != 0) {
                return false;
            }
        }
        return true;
    }

/*TH2: Xet duyet cac duong di theo chieu ngang, doc trong pham vi chu
     nhat */


    private boolean checkRectX(Point p1, Point p2) {
        System.out.println("check rect x");
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        for (int y = pMinY.y; y <= pMaxY.y; y++) {
            if (y > pMinY.y && matrix[pMinY.x][y] != 0) {
                return false;
            }
            // check two or three line
            if ((matrix[pMaxY.x][y] == 0 || matrix[pMaxY.x][y] == matrix[pMaxY.x][pMaxY.y])
                    && checkLineY(pMinY.x, pMaxY.x, y)
                    && checkLineX(y, pMaxY.y, pMaxY.x)) {
                paths.add(pMinY);
                paths.add(new Point(pMinY.x, y));
                paths.add(new Point(pMaxY.x, y));
                paths.add(pMaxY);
                return true;
            }
        }
        // have a line in three line not true then return -1
        return false;
    }

    private boolean checkRectY(Point p1, Point p2) {
        System.out.println("check rect y");
        // find point have y min
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        // find line and y begin
        for (int x = pMinX.x; x <= pMaxX.x; x++) {
            if (x > pMinX.x && matrix[x][pMinX.y] != 0) {
                return false;
            }
            if ((matrix[x][pMaxX.y] == 0 || matrix[x][pMaxX.y] == matrix[pMaxX.x][pMaxX.y])
                    && checkLineX(pMinX.y, pMaxX.y, x)
                    && checkLineY(x, pMaxX.x, pMaxX.y)) {

                paths.add(pMinX);
                paths.add(new Point(x, pMinX.y));
                paths.add(new Point(x, pMaxX.y));
                paths.add(pMaxX);
                return true;
            }
        }
        return false;
    }

    /*TH3: Xet mo rong theo hang ngang, hang doc*/
    //Xet theo chieu ngang
    //type = -1 la di sang trai type=1 la di sang phai
    private boolean checkMoreLineX(Point p1, Point p2, int type) {

        // find point have y min
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        // find line and y begin
        int y = pMaxY.y + type;
        int row = pMinY.x;
        int colFinish = pMaxY.y;
        if (type == -1) {
            colFinish = pMinY.y;
            y = pMinY.y + type;
            row = pMaxY.x;
        }

        // find column finish of line
        // check more
        if ((matrix[row][colFinish] == 0 || pMinY.y == pMaxY.y)
                && checkLineX(pMinY.y, pMaxY.y, row)) {
            while (matrix[pMinY.x][y] == 0
                    && matrix[pMaxY.x][y] == 0) {
                if (checkLineY(pMinY.x, pMaxY.x, y)) {
                    paths.add(pMinY);
                    paths.add(new Point(pMinY.x, y));
                    paths.add(new Point(pMaxY.x, y));
                    paths.add(pMaxY);
                    return true;
                }
                y += type;
            }
        }
        return false;
    }

    // Xet mo rong theo chieu doc type = 1 ( di len tren) type = -1 (di xuong duoi)
    private boolean checkMoreLineY(Point p1, Point p2, int type) {
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        int x = pMaxX.x + type;
        int col = pMinX.y;
        int rowFinish = pMaxX.x;
        if (type == -1) {
            rowFinish = pMinX.x;
            x = pMinX.x + type;
            col = pMaxX.y;
        }
        if ((matrix[rowFinish][col] == 0|| pMinX.x == pMaxX.x)
                && checkLineY(pMinX.x, pMaxX.x, col)) {
            while (matrix[x][pMinX.y] == 0
                    && matrix[x][pMaxX.y] == 0) {
                if (checkLineX(pMinX.y, pMaxX.y, x)) {
                    paths.add(pMinX);
                    paths.add(new Point(x, pMinX.y));
                    paths.add(new Point(x, pMaxX.y));
                    paths.add(pMaxX);
                    return true;
                }
                x += type;
            }
        }
        return false;
    }


    public PointLine checkTwoPoint(Point p1, Point p2) {
        if (!p1.equals(p2) && matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
            // check line with x
            if (p1.x == p2.x) {
                if (checkLineX(p1.y, p2.y, p1.x)) {
                    paths.add(p1);
                    paths.add(p2);
                    return new PointLine(p1, p2);
                }
            }
            // check line with y
            if (p1.y == p2.y) {
                //paths.clear();
                if (checkLineY(p1.x, p2.x, p1.y)) {
                    paths.add(p1);
                    paths.add(p2);
                    return new PointLine(p1, p2);
                }
            }
            // check in rectangle with x
            if ( checkRectX(p1, p2)) {
                return new PointLine(p1, p2);
            }
            // check in rectangle with y
            if (checkRectY(p1, p2)) {
                return new PointLine(p1, p2);
            }
            // check more right
            if (checkMoreLineX(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            // check more left
            if (checkMoreLineX(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }
            // check more down
            //paths.clear();
            if (checkMoreLineY(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            // check more up
            //paths.clear();
            if (checkMoreLineY(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }
        }
        return null;
    }
}