import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sriram on 22-04-2017.
 */
@RunWith(JUnit4.class)
public class PA3Test extends TestCase {

    // Enter student name here before you run the tests
    static String studentName = "Test Student";

    public static final int TIMEOUT_MINUTES = 10;

    // replace this with absolute path if running from command prompt
    static String projectDirectory = System.getProperty("user.dir");

    // we use an error collector so that we can check all the asserts in a test before exiting. Otherwise, the
    // test would exit on first failure
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    static StringBuilder commentsBuilder = new StringBuilder("");
    static String inputDirectory = projectDirectory + "/input/";
    static float totalPoints = 300;
    static HashMap<String,Integer> pointsDirectory = new HashMap<>();

    public static final String INVALID_CUT = "invalidCut";
    public static final String EXCEPTION_CUT = "exceptionCut";
    public static final String VALID_INCORRECT_CUT = "validIncorrectCut";
    public static final String MIN_COST_BAD_RUNTIME = "minCostBadRuntime";
    public static final String INVALID_ALIGNMENT = "invalidAlignment";
    public static final String INCORRECT_ALIGNMENT = "incorrectAlignment";
    public static final String EXCEPTION_ALIGNMENT = "exceptionAlignment";
    public static final String ALIGNMENT_BAD_RUNTIME = "alignmentBadRuntime";
    public static final String EXCEPTION_IMAGE_PROCESSOR = "exceptionImageProcessor";

    static {
        pointsDirectory.put(INVALID_CUT, 5);
        pointsDirectory.put(EXCEPTION_CUT, 5);
        pointsDirectory.put(VALID_INCORRECT_CUT, 5);
        pointsDirectory.put(MIN_COST_BAD_RUNTIME, 50);
        pointsDirectory.put(INVALID_ALIGNMENT, 5);
        pointsDirectory.put(INCORRECT_ALIGNMENT, 5);
        pointsDirectory.put(EXCEPTION_ALIGNMENT, 5);
        pointsDirectory.put(ALIGNMENT_BAD_RUNTIME, 50);
        pointsDirectory.put(EXCEPTION_IMAGE_PROCESSOR, 25);
    }

    private void checkMinCostVC(int expectedMinCost, int[][] matrix, String testName) {
        try {
            ArrayList<Integer> cut = DynamicProgramming.minCostVC(matrix);
            if(cut == null || cut.size() < 1) {
                updatePoints(pointsDirectory.get(INVALID_CUT),"Empty List returned for DynamicProgramming.minCostVC for test - " + testName);
                Assert.fail();
            }
            if(isValidCut(cut, matrix)){
                if(getCutCost(cut,matrix) > expectedMinCost) {
                    updatePoints(pointsDirectory.get(VALID_INCORRECT_CUT),"Incorrect cut returned for DynamicProgramming.minCostVC for test - " + testName);
                    Assert.fail();
                }
            } else {
                updatePoints(pointsDirectory.get(INVALID_CUT),"Invalid cut returned for DynamicProgramming.minCostVC for test - " + testName);
                Assert.fail();
            }
        } catch (Exception e) {
            updatePoints(pointsDirectory.get(EXCEPTION_CUT),"Exception calling DynamicProgramming.minCostVC for test - " + testName + ". Exception - " + e.getClass().getCanonicalName());
            Assert.fail();
        }
    }

    private static void updatePoints(Integer deduct, String comments) {
        totalPoints -= deduct;
        commentsBuilder.append(comments);
        commentsBuilder.append("(-");
        commentsBuilder.append(deduct);
        commentsBuilder.append(" points). ");
    }

    private int getCutCost(ArrayList<Integer> cut, int[][] matrix) {
        int cost = 0;
        for(int i=0; i<cut.size(); i+=2) {
            cost += matrix[cut.get(i)][cut.get(i+1)];
        }
        return cost;
    }

    private boolean isValidCut(ArrayList<Integer> cut, int[][] matrix) {
        int rowSize = matrix.length;
        int colSize = matrix[0].length;
        // if the cut has an odd length, then it cannot have pairs of points
        if(cut.size()%2 != 0) return false;
        // we put all points in a TreeMap in case it is not returned in row order
        TreeMap<Integer,Integer> orderedCut = new TreeMap<>();
        for(int i=0; i < cut.size(); i+=2) {
            int x = cut.get(i);
            int y = cut.get(i+1);
            // invalid indices
            if(x >= rowSize || y >= colSize) {
                return false;
            }
            orderedCut.put(x,y);
        }
        int prevY = -1;
        for(int i=0; i<rowSize; i++) {
            Integer y = orderedCut.get(i);
            // some row was skipped
            if ( y == null ) {
                return false;
            }
            // invalid cut
            if(prevY > 0 && !((y == prevY) || (y == prevY - 1) || y == (prevY + 1))) {
                return false;
            }
            prevY = y;
        }
        return true;
    }

    private int[][] get2DMatrixFromFile(String fileName) {
        int[][] matrix = new int[1][];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            List<String[]> allLines = new ArrayList<>();
            while(line != null && !line.trim().equals("")) {
                allLines.add(line.split("\\s+"));
                line = reader.readLine();
            }
            matrix = new int[allLines.size()][];
            for(int i=0; i<allLines.size(); i++) {
                String[] row = allLines.get(i);
                matrix[i] = new int[row.length];
                for(int j = 0; j < row.length; j++) {
                    matrix[i][j] = Integer.parseInt(row[j]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        return matrix;
    }

    @Test
    public void testMinCostVC1() {
        int expectedMinCost = 32;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-1.txt");
        String testName = "testMinCostVC1";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC2() {
        int expectedMinCost = 105;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-2.txt");
        String testName = "testMinCostVC2";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC3() {
        int expectedMinCost = 88;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-3.txt");
        String testName = "testMinCostVC3";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC4() {
        int expectedMinCost = 110;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-4.txt");
        String testName = "testMinCostVC4";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC5() {
        int expectedMinCost = 35;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-5.txt");
        String testName = "testMinCostVC5";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC6() {
        int expectedMinCost = 159;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-6.txt");
        String testName = "testMinCostVC6";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC7() {
        int expectedMinCost = 222;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-7.txt");
        String testName = "testMinCostVC7";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC8() {
        int expectedMinCost = 162;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-8.txt");
        String testName = "testMinCostVC8";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC9() {
        int expectedMinCost = 213;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-9.txt");
        String testName = "testMinCostVC9";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostVC10() {
        int expectedMinCost = 191;
        int[][] matrix = get2DMatrixFromFile(inputDirectory + "matrix-10.txt");
        String testName = "testMinCostVC10";
        checkMinCostVC(expectedMinCost, matrix, testName);
    }

    @Test
    public void testMinCostRunTime() {
        try {
            // we create a new thread and interrupt it if it runs more than the allowed time
            final ExecutorService service = Executors.newSingleThreadExecutor();
            final Future<?> f = service.submit(new TimedBoundMinCostVCTestingService());
            f.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);
            service.shutdown();
        } catch(Exception e) {
            updatePoints(pointsDirectory.get(MIN_COST_BAD_RUNTIME),"minCostVC was too slow for large matrix");
            Assert.fail();
        }
    }

    private void checkStringAlignment(String x, String y, Integer expectedCost) {
        try {
            String z = DynamicProgramming.stringAlignment(x,y);
            Integer maxLength = x.length() > y.length() ? x.length() : y.length();
            String smallerString = x.length() >= y.length() ? y : x;
            String largerString = x.length() >= y.length() ? x : y;
            if(!isAlignmentValid(z,smallerString,maxLength)) {
                updatePoints(pointsDirectory.get(INVALID_ALIGNMENT), "stringAlignment() returned an invalid alignment for inputs x = " + x + " , y = " + y + " ");
                Assert.fail();
            }
            if(getAlignmentCost(largerString, z) > expectedCost) {
                updatePoints(pointsDirectory.get(INCORRECT_ALIGNMENT), "stringAlignment() returned an incorrect alignment for inputs x = " + x + " , y = " + y + " ");
                Assert.fail();
            }
        } catch (Exception e) {
            updatePoints(pointsDirectory.get(EXCEPTION_CUT),"Exception calling DynamicProgramming.stringAlignment for inputs x = " + x + " , y = " + y + ". Exception - " + e.getClass().getCanonicalName());
            Assert.fail();
        }
    }

    private Integer getAlignmentCost(String largerString, String z) {
        int cost = 0;
        for (int i=0; i<largerString.length(); i++) {
            if(z.charAt(i) == '$') {
                cost += 4;
            } else if(largerString.charAt(i) != z.charAt(i)) {
                cost += 2;
            }
        }
        return cost;
    }

    private boolean isAlignmentValid(String z, String y, Integer maxLength) {
        if(z.length() != maxLength) {
            return false;
        }
        String deAligned = z.replaceAll("\\$","");
        return deAligned.equals(y);
    }

    @Test
    public void testStringAlign1() {
        String x = "ABC";
        String y = "DEF";
        Integer expectedCost = 6;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign2() {
        String x = "ABCDEF";
        String y = "X";
        Integer expectedCost = 22;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign3() {
        String x = "ABCDEF";
        String y = "A";
        Integer expectedCost = 20;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign4() {
        String x = "ACBTACGT";
        String y = "ACGT";
        Integer expectedCost = 16;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign5() {
        String x = "AACAGTTACC";
        String y = "TAAGGTCA";
        Integer expectedCost = 14;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign6() {
        String x = "ACTACT";
        String y = "ACGT";
        Integer expectedCost = 10;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign7() {
        String x = "ATEAXYCXE";
        String y = "ABCDE";
        Integer expectedCost = 20;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign8() {
        String x = "ABCDE";
        String y = "AEDC";
        Integer expectedCost = 8;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign9() {
        String x = "ADGKLPX";
        String y = "DAGKPX";
        Integer expectedCost = 8;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlign10() {
        String x = "ABCDEF";
        String y = "XYZ";
        Integer expectedCost = 18;
        checkStringAlignment(x, y, expectedCost);
    }

    @Test
    public void testStringAlignmentRunTime() {
        try {
            // we create a new thread and interrupt it if it runs more than the allowed time
            final ExecutorService service = Executors.newSingleThreadExecutor();
            final Future<?> f = service.submit(new TimedBoundStringAlignmentTestingService());
            f.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);
            service.shutdown();
        } catch(Exception e) {
            updatePoints(pointsDirectory.get(ALIGNMENT_BAD_RUNTIME),"StringAlignment was too slow for large strings");
            Assert.fail();
        }
    }

    private void reduceWidthAndDisplayImage(String fileName, double trimWidth, String test) {
        try {
            ImageProcessor imageProcessor = new ImageProcessor(fileName);
            Picture pic = imageProcessor.reduceWidth(trimWidth);
            pic.show();
            pic.save(test + ".png");
        } catch (Exception e) {
            updatePoints(pointsDirectory.get(EXCEPTION_IMAGE_PROCESSOR),"Exception testing ImageProcessor. Exception - " + e.getClass().getCanonicalName());
            Assert.fail();
        }
    }

    @Test
    public void testGraphProcessor1() {
        String fileName = inputDirectory + "FirstPic.png";
        double trimWidth = 0.85;
        String test = "testGraphProcessor1";
        reduceWidthAndDisplayImage(fileName, trimWidth, test);
    }

    @Test
    public void testGraphProcessor2() {
        String fileName = inputDirectory + "secondPic.png";
        double trimWidth = 0.85;
        String test = "testGraphProcessor2";
        reduceWidthAndDisplayImage(fileName, trimWidth, test);
    }

    @Test
    public void testGraphProcessor3() {
        String fileName = inputDirectory + "ThirdPic.png";
        double trimWidth = 0.85;
        String test = "testGraphProcessor3";
        reduceWidthAndDisplayImage(fileName, trimWidth, test);
    }

    @Test
    public void testGraphProcessor4() {
        String fileName = inputDirectory + "FourthPic.png";
        double trimWidth = 0.85;
        String test = "testGraphProcessor4";
        reduceWidthAndDisplayImage(fileName, trimWidth, test);
    }

    @AfterClass
    public static void writeCommentsAndPoints(){
        try {
            FileWriter csvWriter = new FileWriter("points.csv", true);
            csvWriter.append(studentName + "," + totalPoints + "," + commentsBuilder.toString());
            csvWriter.append('\n');
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Results added to points.csv");
        } catch (Exception e){
            System.out.println("Error writing to file. Write it yourself!");
            System.out.println("Result : " + studentName + "," + totalPoints + "," + commentsBuilder.toString());
        }
    }

    class TimedBoundMinCostVCTestingService implements Runnable {

        public void run() {
            try {
                Long refTime = 0l;
                Long subTime = 0l;
                int[][] matrix = generate2DMatrix(10000);
                subTime += timeForMinCostVC(matrix);
                refTime += timeForRefMinCostVC(matrix);
                matrix = null;
                System.gc();
                matrix = generate2DMatrix(10000);
                subTime += timeForMinCostVC(matrix);
                refTime += timeForRefMinCostVC(matrix);
                matrix = null;
                System.gc();
                matrix = generate2DMatrix(10000);
                subTime += timeForMinCostVC(matrix);
                refTime += timeForRefMinCostVC(matrix);
                matrix = null;
                System.gc();
                Double avgRef = (double)refTime/3.0;
                Double avgSub = (double)subTime/3.0;
                System.out.println("MinCostVC - " + avgRef + ", " + avgSub);
                if(avgSub > (avgRef * 3)) {
                    updatePoints(pointsDirectory.get(MIN_COST_BAD_RUNTIME), "MinCostVC was too slow");
                    Assert.fail();
                }
            } catch (Exception e) {
                updatePoints(pointsDirectory.get(MIN_COST_BAD_RUNTIME), "Exception when timing minCostVS runtime. Exception - " + e.getClass().getCanonicalName());
                Assert.fail();
            }
        }

        private int[][] generate2DMatrix(int size) {
            int[][] matrix = new int[size][size];
            for(int i=0; i<size; i++) {
                for(int j=0; j<size; j++) {
                    matrix[i][j] = (int)(Math.random() * 100);
                }
            }
            return matrix;
        }

        private long timeForRefMinCostVC(int[][] matrix) {
            long time1 = System.currentTimeMillis();
            RefDynamicProgramming.minCostVC(matrix);
            long time2 = System.currentTimeMillis();
            return time2 - time1;
        }

        private long timeForMinCostVC(int[][] matrix) {
            long time1 = System.currentTimeMillis();
            DynamicProgramming.minCostVC(matrix);
            long time2 = System.currentTimeMillis();
            return time2 - time1;
        }

    }

    class TimedBoundStringAlignmentTestingService implements Runnable {

        public void run() {
            try {
                Long refTime = 0l;
                Long subTime = 0l;
                String[] a = getStringsFromFile(inputDirectory + "largeString-1.txt");
                subTime += timeForStringAlignment(a);
                refTime += timeForRefStringAlignment(a);
                a = null;
                System.gc();
                a = getStringsFromFile(inputDirectory + "largeString-2.txt");
                subTime += timeForStringAlignment(a);
                refTime += timeForRefStringAlignment(a);
                a = null;
                System.gc();
                a = getStringsFromFile(inputDirectory + "largeString-3.txt");
                subTime += timeForStringAlignment(a);
                refTime += timeForRefStringAlignment(a);
                a = null;
                System.gc();
                Double avgRef = (double)refTime/3.0;
                Double avgSub = (double)subTime/3.0;
                System.out.println("String Alignment - " + avgRef + ", " + avgSub);
                if(avgSub > (avgRef * 3)) {
                    updatePoints(pointsDirectory.get(ALIGNMENT_BAD_RUNTIME), "StringAlignment() was too slow");
                    Assert.fail();
                }
            } catch (Exception e) {
                updatePoints(pointsDirectory.get(ALIGNMENT_BAD_RUNTIME), "Exception when timing StringAlignment runtime. Exception - " + e.getClass().getCanonicalName());
                Assert.fail();
            }
        }

        private long timeForRefStringAlignment(String[] a) {
            long time1 = System.currentTimeMillis();
            RefDynamicProgramming.stringAlignment(a[0],a[1]);
            long time2 = System.currentTimeMillis();
            return time2 - time1;
        }

        private long timeForStringAlignment(String[] a) {
            long time1 = System.currentTimeMillis();
            DynamicProgramming.stringAlignment(a[0],a[1]);
            long time2 = System.currentTimeMillis();
            return time2 - time1;
        }

        private String[] getStringsFromFile(String fileName) {
            String[] lines = new String[2];
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                lines[0] = reader.readLine();
                lines[1] = reader.readLine();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(2);
            }
            return lines;
        }

    }
}
