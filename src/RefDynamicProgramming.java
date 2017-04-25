import java.util.*;

public class RefDynamicProgramming {

    public static ArrayList<Integer> minCostVC(int[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        String[][] directions = new String[rows][col];
        int[][] vcCost = new int[rows][col];
        for(int j = 0; j<col; j++)
            vcCost[0][j] = matrix[0][j];
        for(int i = 1; i<rows; i++){
            for(int j=0; j<col; j++) {
                int min = Integer.MAX_VALUE;
                String direction = "";
                if (j==0) {
                    if (vcCost[i-1][j]<vcCost[i-1][j+1])
                        direction = "up";
                    else
                        direction = "right";

                    min = Math.min(vcCost[i-1][j], vcCost[i-1][j+1]);

                }
                if (j==col-1) {
                    if (vcCost[i-1][j-1]<vcCost[i-1][j])
                        direction = "left";
                    else
                        direction = "up";
                    min = Math.min(vcCost[i-1][j], vcCost[i-1][j-1]);
                }
                if (j !=0 && j!=col-1) {

                    min = Math.min(vcCost[i-1][j-1], vcCost[i-1][j]);
                    min = Math.min(min, vcCost[i-1][j+1]);
                    if (min==vcCost[i-1][j-1])
                        direction = "left";
                    else {
                        if (min==vcCost[i-1][j])
                            direction="up";
                        else
                            direction = "right";
                    }

                }

                vcCost[i][j] = (min+matrix[i][j]);
                directions[i][j] = direction;

            }
        }//end Loop

        //System.out.println("Done DC LOoop");

        int minimum = Integer.MAX_VALUE;
        int minIndex = -1;
        for(int j = 0; j<col; j++) {
            if (vcCost[rows-1][j]< minimum) {
                minimum = vcCost[rows-1][j];
                minIndex = j;
            }
        }//end find mincost

        //System.out.println("Done MiNCOst Index");;
		/*for(int i=0; i<rows; i++){
			for(int j =0; j<col; j++)
				System.out.print(vcCost[i][j] + " ");
			System.out.println();


		}*/

	/*	for(int i=0; i<rows; i++){
			for(int j =0; j<col; j++)
				System.out.print(directions[i][j] + " ");
			System.out.println();


		}*/


        ArrayList<Integer> list = new ArrayList<Integer>();
        int colNumber = minIndex;
        //System.out.println(colNumber);;
        list.add(colNumber);
        for(int i = rows-2; i>=0; i--) {
            String s = directions[i+1][colNumber];
            if (s.equals("up")){
                colNumber = colNumber;
                list.add(colNumber);
                //System.out.println(colNumber);;

            }
            if (s.equals("right")){
                colNumber =colNumber+1;
                list.add(colNumber);
                //System.out.println(colNumber);
            }
            if (s.equals("left")){
                colNumber = colNumber-1;
                list.add(colNumber);
                //System.out.println(colNumber);
            }



        }

        ArrayList<Integer> finalList = new ArrayList<Integer>();
        for(int i =list.size()-1; i>=0; i--)
            finalList.add(list.get(i));


        ArrayList<Integer> realFinalList = new ArrayList<>();
        for(int i = 0; i<finalList.size(); i++){
            realFinalList.add(i);
            realFinalList.add(finalList.get(i));
        }
        return realFinalList;
    }

    public static String stringAlignment(String x, String y) {

        int matchCost = 0;
        int misMatchCost = 2;
        int insertCost = 4;

        int n = x.length();
        int m = y.length();

        int[][] cost = new int[m+1][n+1];
        String[][] direction = new String[m][n];
        cost[0][0] = 0;
        for(int i=1; i<n+1; i++) {
            cost[0][i] = cost[0][i-1] + insertCost;
        }
        for(int i=1; i<m+1; i++) {
            cost[i][0] = cost[i-1][0] + insertCost;
        }
        for(int i=1; i<m+1; i++) {
            for(int j=1; j<n+1; j++) {
                int alignCost = (x.charAt(j-1) == y.charAt(i-1) ? matchCost : misMatchCost);
                if((cost[i][j-1] + insertCost) < cost[i-1][j-1] + alignCost) {
                    direction[i-1][j-1] = "right";
                } else {
                    direction[i-1][j-1] = "diag";
                }
                cost[i][j] = Math.min( (cost[i][j-1] + insertCost) , cost[i-1][j-1] + alignCost);
            }
        }
        String z = "";
        while(z.length() < x.length()) {
            if(direction[m-1][n-1].equals("diag")) {
                z += y.charAt(m-1);
                m--;
                n--;
            } else {
                z += "$";
                n--;
            }
            if(m == 0) {
                for(int i=0; i<n ;i++) {
                    z += "$";
                }
                break;
            }
        }
        StringBuilder builder = new StringBuilder(z);
        return builder.reverse().toString();
    }

}
