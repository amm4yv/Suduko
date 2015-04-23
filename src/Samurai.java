import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Samurai extends JFrame implements ActionListener {

	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(contentPane);
	private JButton[][] grid;
	private int gridSize;
	private int boxSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final Samurai frame = new Samurai(9);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Samurai(int size) {
		this.gridSize = size;
		this.boxSize = (int) Math.sqrt(gridSize);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
	}

	public void initialize() {

		int gridWidth = gridSize * 25 > 1000 ? gridSize * 25 : 1000;
		int gridHeight = (gridSize * 25 + 132) > 600 ? (gridSize * 25 + 132)
				: 600;
		setBounds(350, 50, gridWidth-450, gridHeight+50);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		contentPane.setPreferredSize(new Dimension(300, 500));
		scrollPane.setPreferredSize(new Dimension(300, 500));

		setContentPane(scrollPane);
		scrollPane.setViewportView(contentPane);

		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(15, 15, 100, 29);
		btnReset.setActionCommand("reset");
		btnReset.addActionListener(this);
		contentPane.add(btnReset);

		JButton btnGenerateEasy = new JButton("Generate Easy Puzzle");
		btnGenerateEasy.setBounds(120, 15, 180, 29);
		btnGenerateEasy.setActionCommand("generateEasy");
		btnGenerateEasy.addActionListener(this);
		contentPane.add(btnGenerateEasy);

		JButton btnGenerateHard = new JButton("Generate Hard Puzzle");
		btnGenerateHard.setBounds(310, 15, 180, 29);
		btnGenerateHard.setActionCommand("generateHard");
		btnGenerateHard.addActionListener(this);
		contentPane.add(btnGenerateHard);
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.setBounds(15, 50, 117, 29);
		btnSolve.setActionCommand("solve");
		btnSolve.addActionListener(this);
		contentPane.add(btnSolve);

		JButton btnCheck = new JButton("Check Solution");
		btnCheck.setBounds(135, 50, 200, 29);
		btnCheck.setActionCommand("checkSolution");
		btnCheck.addActionListener(this);
		contentPane.add(btnCheck);

		grid = new JButton[gridSize * 2 + boxSize][gridSize * 2 + boxSize];

		MatteBorder top = BorderFactory.createMatteBorder(3, 1, 1, 1,
				Color.GRAY);
		MatteBorder topLeft = BorderFactory.createMatteBorder(3, 3, 1, 1,
				Color.GRAY);
		MatteBorder left = BorderFactory.createMatteBorder(1, 3, 1, 1,
				Color.GRAY);

		for (int x = 0; x < gridSize * 2 + boxSize; x++) {
			for (int y = 0; y < gridSize * 2 + boxSize; y++) {

				// if (!(((x >= boxSize*boxSize && x <= boxSize*boxSize +
				// (boxSize-1))
				// && ((y >= 0 && y <= ((boxSize-1)*boxSize)-1) || (y >=
				// boxSize*5)))
				// || ((y >= boxSize*boxSize && y <= boxSize*boxSize +
				// (boxSize-1))
				// && ((x >= 0 && x <= ((boxSize-1)*boxSize)-1) || (x >=
				// boxSize*5))))) {

				if (!(((x >= 9 && x <= 11) && ((y >= 0 && y <= 5) || (y >= 15))) || ((y >= 9 && y <= 11) && ((x >= 0 && x <= 5) || (x >= 15))))) {

					final JButton temp = new JButton("");
					if (x != 0 && y != 0 && x % boxSize == 0
							&& y % boxSize == 0) {
						temp.setBorder(topLeft);
					} else if (y != 0 && y % boxSize == 0) {
						temp.setBorder(top);
					} else if (x != 0 && x % boxSize == 0) {
						temp.setBorder(left);
					}

					// 0<=x<=12 and (0<=y<=6 or 16<=y<=21)
					// (0<=x<=6 or 16<=x<=21) and 0<=y<=12

					temp.setBackground(Color.LIGHT_GRAY);
					temp.setBounds(6 + (x * 25), 82 + (y * 25), 25, 25);
					temp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							incrementButton(temp);
						}
					});
					contentPane.add(temp);
					grid[y][x] = temp;
				}
			}
		}

		int x = gridSize * 30 / 2 - 175;
		int y = (gridSize * 2 + boxSize) * 30 + 92;


	}

	public void reset() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid.length; y++) {
				if (grid[y][x] != null) {
					grid[y][x].setText("");
					grid[y][x].setEnabled(true);
				}
			}
		}
	}

	public void incrementButton(JButton buttonPressed) {
		String val = buttonPressed.getText();
		if (val.isEmpty()) {
			buttonPressed.setText("1");
		} else if (val.equals(gridSize + "")) {
			buttonPressed.setText("");
		} else {
			int currentValue = Integer.parseInt(val);
			buttonPressed.setText(++currentValue + "");
		}
	}

	public void generateEasy() {
		reset();
		Scanner file;
		try {
			file = new Scanner(new File("puzzlesamurai.txt"));

			int rowNumber = 0;
			while (file.hasNextLine()) {
				String line = file.nextLine();
				if (!line.isEmpty()) {
					String[] row = line.split("");
					JButton[] temp = new JButton[21];
					for (int i = 1; i < row.length; i++) {
						if (row[i].equals("-"))
							temp[i-1] = null;
						else {
							final JButton tempBtn = grid[rowNumber][i-1];
							//System.out.println("i:" + (i) + " rowNumber:" + rowNumber);
							//System.out.println("row[i]:" + (row[i]));
							if (tempBtn != null && !row[i].equals("0")) {
								tempBtn.setText(row[i]);
								tempBtn.setEnabled(false);
							}
							temp[i-1] = tempBtn;
						}
					}
					grid[rowNumber] = temp;
					rowNumber++;
				}
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void generateHard() {
		reset();
		Scanner file;
		try {
			file = new Scanner(new File("puzzle" + grid.length + "hard.txt"));

			int rowNumber = 0;
			while (file.hasNextLine()) {
				String line = file.nextLine();
				if (!line.isEmpty()) {
					String[] row = line.split("\\s+");
					JButton[] temp = new JButton[row.length];
					for (int i = 0; i < row.length; i++) {
						final JButton tempBtn = grid[rowNumber][i];
						if (!row[i].equals("0")) {
							tempBtn.setText(row[i]);
							tempBtn.setEnabled(false);
						}
						temp[i] = tempBtn;
					}
					grid[rowNumber] = temp;
					rowNumber++;
				}
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public int[] findEmpty(int box) {
		int[] location = { 0, 0 };
		int[] indices = {0, 0};	
		switch (box) {
		//Top Right
		case 1: indices[1] = 12;
				break;
		//Bottom Left
		case 2: indices[0] = 12;
				break;
		//Middle
		case 3: indices[0] = 6;
				indices[1] = 6;
				break;
		//Bottom Right
		case 4: indices[0] = 12;
				indices[1] = 12;
				break;			
		}
		
		for (int col = indices[0]; col < indices[0]+9; col++) {
			for (int row = indices[1]; row < indices[1]+9; row++) {
				// System.out.println(grid[row][col]);
				if (grid[col][row].getText().isEmpty()) {
					location[0] = col;
					location[1] = row;
					return location;
				}
			}
		}
		return null;
	}

	public boolean solve(int box) {

		int[] location = findEmpty(box);
		if (location == null)
			return true;

		int col = location[0];
		int row = location[1];

		// consider digits 1 to 9
		for (int num = 1; num <= 9; num++) {
			// if looks promising
			if (isValid(row, col, num, false, box)) {
				// System.out.println("Valid");
				// make tentative assignment
				grid[col][row].setText(num + "");

				// return, if success, yay!
				if (solve(box))
					return true;

				// failure, unmake & try again
				grid[col][row].setText("");
			}
		}

		return false; // this triggers backtracking
	}

	public boolean isValid(int col, int row, int digit, boolean check, int box) {

		int initialCol = 0;
		int initialRow = 0;
		switch (box) {
		//Top Right
		case 1: initialCol = 12;
				break;
		//Bottom Left
		case 2: initialRow = 12;
				break;
		//Middle
		case 3: initialRow = 6;
				initialCol = 6;
				break;
		//Bottom Right
		case 4: initialRow = 12;
				initialCol = 12;
				break;			
		}
		
		
		for (int i = initialCol; i < initialCol+9; i++) {
			if (grid[row][i] != null) {
				if (i != col && grid[row][i].getText().equals(digit + "")) {
					if (check)
						System.out.println("1st " + row + i);
					return false;
				}
			}
		}

		for (int i = initialRow; i < initialRow+9; i++) {
			if (grid[i][col] != null) {
				if (i != row && grid[i][col].getText().equals(digit + "")) {
					if (check)
						System.out.println("2nd " + i + col);
					return false;
				}
			}
		}

		// Check the square
		int boxSize = (int) Math.sqrt(9);

		int newRow = row - row % boxSize;
		int newCol = col - col % boxSize;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i + newRow][j + newCol] != null) {
					if (!(i + newRow == row && j + newCol == col)
							&& grid[i + newRow][j + newCol].getText().equals(
									digit + "")) {
						if (check)
							System.out.println("3rd " + (i + row) + (j + col));
						return false;
					}
				}
			}
		}
		//Top Left
		if ((row < 9 && row > 5) && (col < 9 && col > 5)){
			switch(box){
			//case 0: return isValid(row, col, digit, check, 3);
			case 3: return isValid(col, row, digit, check, 0);
			}
		}
		//Top Right
		if ((row < 9 && row > 5) && (col > 11 && col < 15)){
			switch(box){
			//case 1: return isValid(row, col, digit, check, 3);
			case 3: return isValid(col, row, digit, check, 1);
			}
		}
		//Bottom Left
		if ((row > 11 && row < 15) && (col < 9 && col > 5)){
			switch(box){
			//case 2: return isValid(row, col, digit, check, 3);
			case 3: return isValid(col, row, digit, check, 2);
			}
		}
		//Bottom Right
		if ((row > 11 && row < 15) && (col > 11 && col < 15)) {
			switch(box){
			//case 4: return isValid(row, col, digit, check, 3);
			case 3: return isValid(col, row, digit, check, 4);
			}
		}
		return true;
	}

	public boolean solveOne(int box){
		int validDigit = 0;
		int numValid = 0;
		int[] indices = {0, 0};	
		switch (box) {
		case 1: indices[1] = 12;
				break;
		case 2: indices[0] = 12;
				break;
		case 3: indices[0] = 6;
				indices[1] = 6;
				break;
		case 4: indices[0] = 12;
				indices[1] = 12;
				break;			
		}
		for (int row = indices[0]; row < indices[0]+9; row++) {
	        for (int col = indices[1]; col < indices[1]+9; col++) {
				for (int num = 1; num <= 9; num++){
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
						if (isValid(row, col, num, false, box)){
				        	numValid++;
				        	validDigit = num;
				        }
					}
				}
				if(numValid == 1){
					grid[row][col].setText(validDigit + "");
				}
				numValid = 0;
	        }
		}
		return true;
	}
	
	public boolean solveSections(int box){
		int validIndex = 0;
		int numValid = 0;
		int[] indices = {0, 0};	
		switch (box) {
		case 1: indices[1] = 12;
				break;
		case 2: indices[0] = 12;
				break;
		case 3: indices[0] = 6;
				indices[1] = 6;
				break;
		case 4: indices[0] = 12;
				indices[1] = 12;
				break;			
		}
		for (int num = 1; num <= 9; num++){
			for (int row = indices[0]; row < indices[0]+9; row++) {
				for (int col = indices[1]; col < indices[1]+9; col++) {			
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
				        if (isValid(row, col, num, false, box)){
				        	numValid++;
				        	validIndex = col;
				        }
					}
				}
				if(numValid == 1){
					grid[row][validIndex].setText(num + "");
				}
				numValid = 0;
	        }
			
			for (int col = indices[1]; col < indices[1]+9; col++) {
				for (int row = indices[0]; row < indices[0]+9; row++) {			
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
				        if (isValid(row, col, num, false, box)){
				        	numValid++;
				        	validIndex = row;
				        }
					}
				}
				if(numValid == 1){
					grid[validIndex][col].setText(num + "");
				}
				numValid = 0;
	        }
						
			int validRow = 0;
			int validCol = 0;
					
			for (int i = 0; i < 3; i++){
				for (int row = indices[0]; row < indices[0]+3; row++){
					for (int col = indices[1]; col < indices[1]+3; col++){
						if (grid[row+(i*boxSize)][col+(i*boxSize)].getText().isEmpty()) {
					        if (isValid(row+(i*boxSize), col+(i*boxSize), num, false, box)){
					        	numValid++;
					        	validRow = row;
					        	validCol = col;
					        }
						}
					}
				}
				if(numValid == 1){
					grid[validRow][validCol].setText(num + "");
				}
				numValid = 0;
			}
			
		}
		return true;
	}
	
	
	public void checkSolution() {
		// Check for empty tiles
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				String value = grid[x][y].getText();
				if (value.isEmpty()) {
					JOptionPane.showMessageDialog(this,
							"The puzzle has not been completed.");
					return;
				}
			}
		}
		// Grid filled, check solution
//		for (int x = 0; x < gridSize; x++) {
//			for (int y = 0; y < gridSize; y++) {
//				String value = grid[x][y].getText();
//				if (!isValid(x, y, Integer.parseInt(value), true)) {
//					JOptionPane.showMessageDialog(this,
//							"The solution is incorrect.");
//					System.out.println(x + ", " + y + ": "
//							+ Integer.parseInt(value));
//					return;
//				}
//			}
//		}

		JOptionPane.showMessageDialog(this, "The solution is correct!");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("generateEasy")) {
			generateEasy();
		}
		if (action.equals("generateHard")) {
			generateHard();
		}
		if (action.equals("reset")) {
			reset();
		}
		if (action.equals("solve")) {
			boolean success = false;
			
//			int timeout = 50;
//			boolean success = true;
//			while (findEmpty(0) != null){
//				solveOne(0);
//				solveSections(0);
//				timeout--;
//				if(timeout == 0)
//					break;
//			}
//			if(findEmpty(0) != null){
//				//generatePossibilities(0);
//				success = solve(0);
//			}
			
			success = solve(3);
			
			//success = solve(0);
			
			//success = solve(2);
			success = solve(1);
			//success = solve(4);
			
			
			
			//print();
//			for(int i = 0; i < 5; i++){
//				solveOne(i);
//				solveSections(i);
//				solve(i);
//				//print();
//			}
			//success = solve(0);
			if (!success) {
				JOptionPane.showMessageDialog(this,
						"There is no solution to this puzzle.");
			} else {
				//print();
			}
		}
		if (action.equals("checkSolution")) {
			checkSolution();
		}

	}

	public void print() {
		int rCounter = 1;
		for (JButton[] row : grid) {
			if (rCounter > grid.length)
				rCounter = 1;
			int cCounter = 1;
			for (JButton num : row) {
				if (num != null){
					System.out.print(num.getText() + " ");
//					if (cCounter % (int) Math.sqrt(grid.length) == 0)
//						System.out.print("\t");
//					cCounter++;
				}
			}

			System.out.print("\n");
			if (rCounter % (int) Math.sqrt(grid.length) == 0)
				System.out.print("\n");
			rCounter++;
		}
	}
}
