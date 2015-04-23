import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuSolver {

	public static void main(String[] args) throws FileNotFoundException {

		int[][] grid = readInput("puzzle9hard.txt", 9);

		print(grid);
		
//		System.out.print("\nSOLUTION\n");
//		int[][] solved = solve(grid);
//				
//		print(solved);
//		
//		System.out.print("\nSOLUTION BACK\n");
//		int[][] solvedBack = solveBack(grid);
//		int[][] solvedBack2 = solveBack(solvedBack);
//		int[][] solvedBack3 = solveBack(solvedBack2);
//		int[][] solvedBack4 = solveBack(solvedBack3);
//		print(solvedBack4);
//		
		int timeout = 100;
		while (findEmpty(grid) != null){
			int[][] temp = solveOne(grid);
			int[][] temp2 = solveSections(temp);
			grid = temp2;
			timeout--;
			if(timeout == 0)
				break;
		}
		if(findEmpty(grid) != null)
			solveRecurse(grid);
		
		//System.out.print("\nSOLUTION RECURSIVE\n");
		//boolean solvedRecurse = solveRecurse(grid);
		//System.out.println(solvedRecurse);
		print(grid);

	}

	public static int[][] readInput(String filename, int gridSize)
			throws FileNotFoundException {
		int[][] grid = new int[gridSize][gridSize];

		Scanner file = new Scanner(new File(filename));

		int rowNumber = 0;
		while (file.hasNextLine()) {
			String line = file.nextLine();
			if (!line.isEmpty()) {
				String[] row = line.split("\\s+");
				int[] temp = new int[row.length];
				for (int i = 0; i < row.length; i++){
					temp[i] = Integer.parseInt(row[i]);
				}
				grid[rowNumber] = temp;
				rowNumber++;
			}
		}

		return grid;
	}
	
	public static int[][] solveOne(int[][] grid){
		int validDigit = 0;
		int numValid = 0;
		for (int row = 0; row < grid.length; row++) {
	        for (int col = 0; col < grid.length; col++) {
				for (int num = 1; num <= grid.length; num++){
			        // if looks promising
					if (grid[row][col] == 0) {
						if (isValid(grid, row, col, num)){
				        	numValid++;
				        	validDigit = num;
				        }
					}
				}
				if(numValid == 1){
					grid[row][col] = validDigit;
				}
				numValid = 0;
	        }
		}
		return grid;
	}
	
	public static int[][] solveSections(int[][] grid){
		int validIndex = 0;
		int numValid = 0;
		for (int num = 1; num <= grid.length; num++){
			for (int row = 0; row < grid.length; row++) {
				for (int col = 0; col < grid.length; col++) {			
			        // if looks promising
					if (grid[row][col] == 0) {
				        if (isValid(grid, row, col, num)){
				        	numValid++;
				        	validIndex = col;
				        }
					}
				}
				if(numValid == 1){
					grid[row][validIndex] = num;
				}
				numValid = 0;
	        }
			
			for (int col = 0; col < grid.length; col++) {
				for (int row = 0; row < grid.length; row++) {			
			        // if looks promising
					if (grid[row][col] == 0) {
				        if (isValid(grid, row, col, num)){
				        	numValid++;
				        	validIndex = row;
				        }
					}
				}
				if(numValid == 1){
					grid[validIndex][col] = num;
				}
				numValid = 0;
	        }
			
			int boxSize = (int)Math.sqrt(grid.length);
			
			int validRow = 0;
			int validCol = 0;
					
			for (int i = 0; i < boxSize; i++){
				for (int row = 0; row < boxSize; row++){
					for (int col = 0; col < boxSize; col++){
						if (grid[row+(i*boxSize)][col+(i*boxSize)] == 0) {
					        if (isValid(grid, row+(i*boxSize), col+(i*boxSize), num)){
					        	numValid++;
					        	validRow = row;
					        	validCol = col;
					        }
						}
					}
				}
				if(numValid == 1){
					grid[validRow][validCol] = num;
				}
				numValid = 0;
			}
			
		}
		return grid;
	}
	
	/* Searches the grid to find an entry that is still unassigned. If
	   found, the reference parameters row, col will be set the location
	   that is unassigned, and true is returned. If no unassigned entries
	   remain, false is returned. */
	public static int[] findEmpty(int[][] grid){
		int[] location = {0, 0};
	    for (int row = 0; row < grid.length; row++)
	        for (int col = 0; col < grid.length; col++)
	            if (grid[row][col] == 0) {
	            	location[0] = row;
	            	location[1] = col;
	            	return location;
	            }
	    return null;
	}
	
	public static boolean solveRecurse(int[][] grid){
		
		int[] location = findEmpty(grid);
		if (location == null)
			return true;
		
		int row = location[0];
		int col = location[1];
		
		 // consider digits 1 to 9
	    for (int num = 1; num <= grid.length; num++){
	        // if looks promising
	        if (isValid(grid, row, col, num)){
	        	//System.out.println("Valid");
	            // make tentative assignment
	            grid[row][col] = num;
	 
	            // return, if success, yay!
	            if (solveRecurse(grid))
	                return true;
	 
	            // failure, unmake & try again
	            grid[row][col] = 0;
	        }
	    }
	    
	    return false; // this triggers backtracking
		
		/**
		 * bool SolveSudoku(int grid[N][N])
{
    int row, col;
 
    // If there is no unassigned location, we are done
    if (!FindUnassignedLocation(grid, row, col))
       return true; // success!
 
    // consider digits 1 to 9
    for (int num = 1; num <= 9; num++)
    {
        // if looks promising
        if (isSafe(grid, row, col, num))
        {
            // make tentative assignment
            grid[row][col] = num;
 
            // return, if success, yay!
            if (SolveSudoku(grid))
                return true;
 
            // failure, unmake & try again
            grid[row][col] = UNASSIGNED;
        }
    }
    return false; // this triggers backtracking
}
		 */
	}
	
	

	/**
	 * A brute force algorithm visits the empty cells in some order, filling in
	 * digits sequentially from the available choices, or backtracking (removing
	 * failed choices) when a dead-end is reached. For example, a brute force
	 * program would solve a puzzle by placing the digit "1" in the first cell
	 * and checking if it is allowed to be there. If there are no violations
	 * (checking row, column, and box constraints) then the algorithm advances
	 * to the next cell, and places a "1" in that cell. When checking for
	 * violations, it is discovered that the "1" is not allowed, so the value is
	 * advanced to a "2". If a cell is discovered where none of the 9 digits is
	 * allowed, then the algorithm leaves that cell blank and moves back to the
	 * previous cell. The value in that cell is then increased by 1. The
	 * algorithm is repeated until a valid solution for all 81 cells is found.
	 */
	
	public static int[][] solve(int[][] grid){
		
		int digit = 1;
		int prevRow = 0;
		int prevCol = 0;
		
		int[][] solution = grid;
		
		for (int row = 0; row < solution.length; row++) {
			for (int col = 0; col < solution.length; col++) {
				
				if (solution[row][col] == 0){
					prevRow = row;
					prevCol = col;
					
					while (!isValid(solution, row, col, digit) && digit <= solution.length){
						digit++;
					}
					
					if (isValid(solution, row, col, digit) && digit <= solution.length)
						solution[row][col] = digit;
					
					else if (solution[prevRow][prevCol] < solution.length){
						solution[prevRow][prevCol]++;
						digit = 1;
						col--;
					}
				}
						
			}
		}
		
		
		return solution;
	}
	
	public static int[][] solveBack(int[][] grid){
		
		int[][] solution = grid;
		
		for (int x = 0; x < solution.length; x++){
			for (int y = 0; y < solution.length; y++){
				
				if (solution[x][y] == 0) {

					for (int value = 1; value <= solution.length; value++) {
						if (isValid(solution, x, y, value)) {
							solution[x][y] = value;
							break;
						}
					}
				}

			}

		}
		
		return solution;
		
		/**
		 * Initialize 2D array with 81 empty grids (nx = 9, ny = 9)
 Fill in some empty grid with the known values
 Make an original copy of the array
 Start from top left grid (nx = 0, ny = 0), check if grid is empty
 if (grid is empty) {
   assign the empty grid with values (i)
   if (no numbers exists in same rows & same columns same as (i) & 3x3 zone (i) is currently in)
     fill in the number
   if (numbers exists in same rows & same columns same as (i) & 3x3 zone (i) is currently in)
     discard (i) and repick other values (i++)
 }
 else {
   while (nx < 9) {
     Proceed to next row grid(nx++, ny)
     if (nx equals 9) {
       reset nx = 1
       proceed to next column grid(nx,ny++)
       if (ny equals 9) {
         print solution
       }
     }
   }
 }
		 */
		
				
	}
	
	public static boolean isValid(int[][] grid, int row, int col, int digit){
				
		for (int i = 0; i < grid.length; i++){
			if (grid[row][i] == digit)
				return false;
		}
		
		for (int i = 0; i < grid.length; i++){
			if (grid[i][col] == digit)
				return false;
		}
		
		int boxSize = (int)Math.sqrt(grid.length);
		
		row -= row % boxSize;
		col -= col % boxSize;

		for (int i = 0; i < boxSize; i++){
			for (int j = 0; j < boxSize; j++){
				if (grid[i+row][j+col] == digit)
					return false;
			}
		}
		
		return true;
	}
	
	public static void print(int[][] grid){
		int rCounter = 1;
		for (int[] row : grid) {
			if (rCounter > grid.length)
				rCounter = 1;
			int cCounter = 1;
			for (int num : row) {
				System.out.print(num + " ");
				if (cCounter % (int)Math.sqrt(grid.length) == 0)
					System.out.print("\t");
				cCounter++;
			}
			
			System.out.print("\n");
			if (rCounter % (int)Math.sqrt(grid.length) == 0)
				System.out.print("\n");
			rCounter++;
		}
	}

}
