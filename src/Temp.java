import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by Sriram on 22-04-2017.
 */
public class Temp {
    public static void main(String[] args) {
        int n = 10000;
        try {
            FileWriter csvWriter = new FileWriter("input/largematrix-3.txt", false);
            for(int i=0; i<n; i++) {
                StringBuilder row = new StringBuilder("");
                for(int j=0; j<n; j++) {
                    Integer number = (int)(Math.random() * 100);
                    row.append(number);
                    if(j!= n-1) {
                        row.append("\t");
                    }
                }
                csvWriter.append(row.toString());
                csvWriter.append(System.lineSeparator());
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
