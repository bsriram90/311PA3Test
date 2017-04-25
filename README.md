# PA3 Grading Script

## Steps to run
1. The necessary junit jars can be found [here](https://github.com/junit-team/junit4/wiki/Download-and-Install).
2. Copy student submission files(except Picture.java) to src folder.
3. Run PA3Test.
4. The sections testing ImageProcessor.
4. Grades and comments will be added to points.csv.
5. Report, Iterative solution and ImageProcessor would need to be graded manually(details below).

## Grading Details
Total Points - 500
1. Report - 50 points
2. MinCostVC - 150 points
  1. Any Iterative Solution - 50 points
  2. Accurate minCostVC (10 tests) - 50 points
  3. Efficient Runtime - 50 points
3. StringAlignment - 150 points
  1. Any Iterative Solution - 50 points
  2. Accurate StringAlignment (10 tests) - 50 points
  3. Efficient Runtime - 50 points
4. ImageProcessor - 150 points
  1. Any Iterative Solution - 50 points
  2. Image width reduced properly(4 tests) - 100 points

Here are grades that should be calulated or adjusted manually
### Report
We only look for the Recurrence relations for minCostVC and stringAlignment. Each carries 25 points
### Iterative Solution
For MinCostVC, StringAlignment and ImageProcessor, if at least one test has passed and the student has attempted an iterative solution, they get 50 points for each section.
### ImageProcessor
The reducedWidth method is tested on 4 images in the 'input' directory. In each image, the band of solid colors must be removed.
The reducedWidth image will be stored as testGraphProcessor1.png,...,testGraphProcessor4.png in the project root directory.
The script assumes that the image is correct and adds the points for each of these tests as long as there is no exception. The points must be deducted manually if the image seems incorrect.
