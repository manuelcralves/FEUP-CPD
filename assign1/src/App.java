import java.util.Scanner;

public class App {
    public static void onMult(int m_ar, int m_br) {

        long Time1, Time2;

        int i, j, k;

        double temp;

        double[][] pha = new double[m_ar][m_ar];
        double[][] phb = new double[m_ar][m_ar];
        double[][] phc = new double[m_ar][m_ar];

        for (i = 0; i < m_ar;i++){
            for (j = 0; j < m_ar; j++){
                pha[i][j] = 1.0;
            }
        }

        for (i = 0; i < m_br;i++) {
            for (j = 0; j < m_br;j++){
                phb[i][j] = i+1;
            }
        }

        Time1 = System.nanoTime();

        for (i = 0; i < m_ar;i++){
            for (j = 0; j < m_br;j++){
                temp = 0;
                for (k = 0; k < m_ar;k++){
                    temp += pha[i][k] * phb[k][j];
                }
                phc[i][j] = temp;
            }
        }

        Time2 = System.nanoTime();

        long Time = (Time2 - Time1)/1000000;

        double TimeDouble = (double) Time / 1000;

        System.out.printf("Time: %3.3f seconds\n",TimeDouble);

        System.out.println("Result Matrix:");
        for (i = 0; i < 1;i++){
            for (j = 0; j < Math.min(10,m_br);j++){
                System.out.print(phc[i][j] + " ");
            }
        }
        System.out.println("\n");
    }

    public static void main(String[] args) throws Exception {
        int lin, col, blockSize;
        int op = 1;

        do {
            System.out.println("1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.println("3. Block Multiplication");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Selection?: ");
            op = scanner.nextInt();
            if (op == 0) break;


            System.out.print("Dimensions: lins=cols ? ");
            lin = scanner.nextInt();
            col = lin;

            switch (op) {
                case 1:
                    onMult(lin,col);
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        } while (op != 0);
    }
}