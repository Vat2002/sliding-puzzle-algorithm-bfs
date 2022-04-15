//student id : 20200765 , w1833511
// name : nammuni vathila vilhan de silva

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class SlidingPuzzle {
    static String directionalMovement="";
    static LinkedList<Point> MazePath =new LinkedList<>(); //used to store the path(co-ordinates)
    static String navigator;
    static ArrayList<String> data = new ArrayList<>(); //arraylist created to store the data from text file
    static  int countRow = 0; //number of rows in the puzzle
    static long timeStart;

    public static void main(String[] args) {

        LoadDataFromFile();

        int countColumn = data.get(0).length();
        char[][] textDataArray = new char[data.size()][countColumn];

        //passing the data from the arraylist to the 2d array
        for (int i = 0; i < countRow; i++) {
            for (int j = 0; j < countColumn; j++) {
                char temp = data.get(i).charAt(j); //the string value of the index in the ith element is passed to a temporary variable
                textDataArray[i][j] = temp;

            }
        }


        int XStartValues = 0;
        int YStartValues = 0;

        for (int i = 0; i < textDataArray.length; i++) { //finding the start point
            for (int j = 0; j < textDataArray[i].length; j++) {

                if (textDataArray[i][j] == 'S') { //checks if the string value in the textDataArray is S
                    XStartValues = j; //the j value is passed to the x start value variable
                    YStartValues = i; //the i value is passed to the x start value variable
                    break;
                }
            }
        }


        int XEndValues = 0;
        int YEndValues = 0;

        for (int i = 0; i < textDataArray.length; i++) { //finding the end point
            for (int j = 0; j < textDataArray[i].length; j++) {

                // Finding source
                if (textDataArray[i][j] == 'F') { //checks if the string value in the textDataArray is F
                    XEndValues = j; //the j value is passed to the x start value variable
                    YEndValues = i ;//the i value is passed to the x start value variable
                    break;
                }
            }
        }


        System.out.println("||||||||||||||||||||MAZE||||||||||||||||||||\n"); //the maze is being created

        for (int i = 0; i < textDataArray.length; i++) {
            for (int j = 0; j < textDataArray[i].length; j++) {
                System.out.print(textDataArray[i][j]); //the maze is being displayed
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Start Point = (" + XStartValues + "," + YStartValues + ")"); //displays the start x and y values(start point)
        System.out.println("End Point = (" + XEndValues + "," + YEndValues + ")"); //displays the end x and y values(end point)
        System.out.println();
        System.out.println(solve(textDataArray, XStartValues, YStartValues, XEndValues, YEndValues)); //calls the solve method and prints the value which is being returned
        System.out.println();

        if(MazePath.size()!=0) { //checks if the size of the linked-list maze path isn't equal to 0

            MazePath.pop(); //remove and return the top element from the stack represented by the LinkedList

            System.out.println("Start at{X=" + XStartValues + ", Y=" + YStartValues + "}"); //displays the start x and y values
            for (int i = 0; i < MazePath.size() - 1; i++) {

                //checks if the current co-ordinates are greater or lesser than next nodes co-ordinates and the move accordingly
                if (MazePath.get(i).x > MazePath.get(i + 1).x) { //if the current node X co-ordinate  is greater than the next node X co-ordinate to be moved it will move left
                    navigator = "Left";
                } else if (MazePath.get(i).x < MazePath.get(i + 1).x) { //if the current node X co-ordinate  is lesser than the next node X co-ordinate to be moved it will move right
                    navigator = "Right";
                } else if (MazePath.get(i).y > MazePath.get(i + 1).y) { //if the current node Y co-ordinate  is greater than the next node Y co-ordinate to be moved it will move up
                    navigator = "Up";
                } else if (MazePath.get(i).y < MazePath.get(i + 1).y) { //if the current node Y co-ordinate  is lesser than the next node Y co-ordinate to be moved it will move down
                    navigator = "Down";
                } else {
                    navigator = "";
                }
                System.out.println("Move " + navigator + "{X=" + MazePath.get(i).x + "," + "Y=" + MazePath.get(i).y + "}");
            }

            for (int i = 0; i < 1; i++) {
                System.out.println("Last move " + "{X=" + MazePath.get(MazePath.size() - 1).x + "," + "Y=" + MazePath.get(MazePath.size() - 1).y + "}");
            }

            System.out.println("End at{X=" + XEndValues + ", Y=" + YEndValues + "}"); //displays the end values of x and y
            System.out.println("Finished!");
        }
        else{
            System.out.println("Path not found!"); //error message displayed if the path isn't found
        }

        long terminatingTime = System.nanoTime() - timeStart;
        System.out.println("Completion time in seconds: " + terminatingTime / 1000000000.0); //displays the executing time in seconds

    }

    public static void LoadDataFromFile(){ //loading data from the given data text files

        System.out.println("Enter the directory of the file: ");
        Scanner read = new Scanner(System.in);
        String path = read.next();
        timeStart = System.nanoTime(); //returns the current value of the system timer in seconds

        try { //gets the file directory to read the data
            File filePath = new File(path);
            Scanner dataReader = new Scanner(filePath);
            while (dataReader.hasNextLine()) {
                data.add(dataReader.nextLine()); //data is being added to the list
                countRow++; //row counter is increased in very line visited
            }
            dataReader.close();
        } catch (FileNotFoundException e) { //if file not found exception is thrown with error message
            System.out.println("ERROR! File not found! ");
            e.printStackTrace();
        }
    }

    public static int solve(char[][] textDataArray, int XStartValues, int YStartValues, int XEndValues, int YEndValues) {

        Point startPosition = new Point(XStartValues, YStartValues); //starting position to find the direction
        LinkedList<Point> listQueue = new LinkedList<>();
        Point[][] slideTraverse = new Point[textDataArray.length][textDataArray[0].length]; //used to store the position of the nodes which is traversed

        listQueue.addLast(new Point(XStartValues, YStartValues));
        slideTraverse[YStartValues][XStartValues] = startPosition; //adding the starting point to the queue

        while (listQueue.size() != 0) { //checks if the queue isn't empty

            Point currPosition = listQueue.pollFirst(); //used to retrieve the first element of the Deque
            directionalMovement="";
            System.out.println("Current position:"+currPosition);

            for (Direction dir : Direction.values()) {     // traversing adjacent nodes while sliding in the direction
                Point nextPosition = move(textDataArray, slideTraverse, currPosition, dir);
                System.out.println("\t" + nextPosition);
                if (nextPosition != null) {
                    listQueue.addLast(nextPosition);
                    slideTraverse[nextPosition.getY()][nextPosition.getX()] = new Point(currPosition.getX(), currPosition.getY());
                    if (nextPosition.getY() == YEndValues && nextPosition.getX() == XEndValues) { //found the end point

                        int count = 0;
                        Point tempValue = nextPosition;
                        while (tempValue != startPosition) {
                            count++;
                            tempValue = slideTraverse[tempValue.getY()][tempValue.getX()];
                            MazePath.addFirst(tempValue);
                        }
                        return count;
                    }
                }
            }
            System.out.println();
        }
        return -1;
    }

    public static Point move(char[][] textDataArray, Point[][] slideTraverse, Point currPosition, Direction dir) {

        int X = currPosition.getX();//getting the position of the current x position
        int Y = currPosition.getY();//getting the position of the current y position

        int xDiff = (dir == Direction.LEFT ? -1 : (dir == Direction.RIGHT ? 1 : 0));  // if the direction equals to the left then it returns -1 if it equals right then it returns -1 if not both then returns 0
        int yDiff = (dir == Direction.UP ? -1 : (dir == Direction.DOWN ? 1 : 0));  // if the direction equals to the up then it returns -1 if it equals down then it returns -1 if not both then returns 0

        switch (xDiff) {  // move left or right according to the direction
            case -1 -> directionalMovement = "Left";
            case 1 -> directionalMovement = "Right";
        }
        switch (yDiff) { // move up or down according to the direction
            case -1 -> directionalMovement = "Up";
            case 1 -> directionalMovement = "Down";
        }

        //continue to slide until it hits a wall
        int i = 1;
        while (X + i * xDiff >= 0
                && X + i * xDiff < textDataArray[0].length
                && Y + i * yDiff >= 0
                && Y + i * yDiff < textDataArray.length
                && !(textDataArray[Y + i * yDiff][X + i * xDiff]=='0')
                && !(textDataArray[Y + (i-1) * yDiff][X + (i-1) * xDiff]=='F')) {
            i++;
        }

        i--;  // reverse the last step

        if (slideTraverse[Y + i * yDiff][X + i * xDiff] != null) {
            return null;   //if this point is already met
        }
        return new Point(X + i * xDiff, Y + i * yDiff); //returns the x and y of the points
    }

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {

            return "Point{" +
                    "X=" + x +
                    ", Y=" + y +
                    '}' +"  "+directionalMovement;

        }
    }

    //create sets of constants for use with variables and properties.
    public enum Direction { // use of enum because we know all possible values at compile-time
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

}