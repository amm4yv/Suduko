import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JRadioButton;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JScrollBar;

public class Example extends JFrame implements ActionListener {

	private JPanel contentPane = new JPanel(); 
	private JScrollPane scrollPane = new JScrollPane(contentPane);
	private JButton[][] grid;
	private int gridSize;
	private ArrayList<Integer>[][] possibilities;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final Example frame = new Example(9);
			 
//					contentPane.setPreferredSize(new Dimension( 2000,2000));
//					JScrollPane scrollFrame = new JScrollPane(contentPane);
//					contentPane.setAutoscrolls(true);
//					scrollFrame.setPreferredSize(new Dimension( 800,300));
//					frame.add(scrollFrame);
					
			        frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void decrease() {
		this.gridSize = (int) Math.pow((int) Math.sqrt(this.gridSize) - 1, 2);
		initialize();
	}

	public void increase() {
		this.gridSize = (int) Math.pow((int) Math.sqrt(this.gridSize) + 1, 2);
		initialize();
	}

	/**
	 * Create the frame.
	 */
	public Example(int size) {
		this.gridSize = size;
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
	}

	public void initialize() {
				
		int gridWidth = gridSize * 30 > 600 ? gridSize * 30 : 600;
		int gridHeight = (gridSize * 30 + 132) > 600 ? (gridSize * 30 + 132) : 600;
		setBounds(100, 100, gridWidth, gridHeight);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		contentPane.setPreferredSize(new Dimension(gridWidth, gridHeight));
		scrollPane.setPreferredSize(new Dimension(600, 600));
		
		// This deos this
		setContentPane(scrollPane);	
		scrollPane.setViewportView(contentPane);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(15, 15, 100, 29);
		btnReset.setActionCommand("reset");
		btnReset.addActionListener(this);
		contentPane.add(btnReset);

		final JButton btnDecSize = new JButton("Decrease Grid Size");
		btnDecSize.setBounds(15, 47, 200, 29);
		if (this.gridSize <= 4) {
			btnDecSize.setEnabled(false);
		}
		btnDecSize.setActionCommand("decreaseSize");
		btnDecSize.addActionListener(this);
		contentPane.add(btnDecSize);

		final JButton btnIncSize = new JButton("Increase Grid Size");
		btnIncSize.setBounds(220, 47, 200, 29);
		if (this.gridSize >= 25) {
			btnIncSize.setEnabled(false);
		}
		btnIncSize.setActionCommand("increaseSize");
		btnIncSize.addActionListener(this);
		contentPane.add(btnIncSize);

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
		 
		possibilities = new ArrayList[gridSize][gridSize];
		grid = new JButton[gridSize][gridSize];
		
		MatteBorder top = BorderFactory.createMatteBorder(3, 1, 1, 1, Color.GRAY);
		MatteBorder topLeft = BorderFactory.createMatteBorder(3, 3, 1, 1, Color.GRAY);
		MatteBorder left = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.GRAY);
				
		int boxSize = (int)Math.sqrt(gridSize);
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				final JButton temp = new JButton("");
				if (x != 0 && y != 0 && x % boxSize == 0 && y % boxSize == 0) {
					temp.setBorder(topLeft);
				}
				else if (y != 0 && y % boxSize == 0) {
					temp.setBorder(top);
				}
				else if (x != 0 && x % boxSize == 0) {
					temp.setBorder(left);
				}
				temp.setBackground(Color.LIGHT_GRAY);
				temp.setBounds(6 + (x * 30), 82 + (y * 30), 30, 30);
				temp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						incrementButton(temp);
					}
				});
				contentPane.add(temp);
				grid[x][y] = temp;
			}
		}

		int x = gridSize * 30 / 2 - 175;
		int y = gridSize * 30 + 92;

		JButton btnSolve = new JButton("Solve");
		btnSolve.setBounds(15, y, 117, 29);
		btnSolve.setActionCommand("solve");
		btnSolve.addActionListener(this);
		contentPane.add(btnSolve);

		JButton btnCheck = new JButton("Check Solution");
		btnCheck.setBounds(135, y, 200, 29);
		btnCheck.setActionCommand("checkSolution");
		btnCheck.addActionListener(this);
		contentPane.add(btnCheck);

	}

	

	public void reset() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid.length; y++) {
				grid[x][y].setText("");
				grid[x][y].setEnabled(true);
			}
		}
	}

	public void incrementButton(JButton buttonPressed) {
		String val = buttonPressed.getText();
		if (val.isEmpty()) {
			buttonPressed.setText("1");
		} else if (val.equals(grid.length + "")) {
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
			file = new Scanner(new File("puzzle" + grid.length + "easy.txt"));

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

	public int[] findEmpty() {
		int[] location = { 0, 0 };
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid.length; col++) {
				// System.out.println(grid[row][col]);
				if (grid[row][col].getText().isEmpty()) {
					location[0] = row;
					location[1] = col;
					return location;
				}
			}
		}
		return null;
	}
	
	public void generatePossibilities() {
		
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid.length; col++) {
				possibilities[row][col] = new ArrayList<Integer>();
				// System.out.println(grid[row][col]);
				if (grid[row][col].getText().isEmpty()) {
					for (int num = 1; num <= grid.length; num++) {
						if (isValid(row, col, num, false)) {
							possibilities[row][col].add(num);
						}
					}
				}
			}			
		}
		
	}

	public boolean solve() {

		int[] location = findEmpty();
		if (location == null)
			return true;

		int row = location[0];
		int col = location[1];

//		int num = possibilities[row][col].get(0);
//		if (isValid(row, col, num, false)) {
//			grid[row][col].setText(num + "");
//			possibilities[row][col].remove(0);
//		}
		
		ArrayList<Integer> values = possibilities[row][col];
		
		// consider digits 1 to 9
		for (int i = 0; i < values.size(); i++) {
			// if looks promising
			int num = values.get(i);
			if (isValid(row, col, num, false)) {
				// System.out.println("Valid");
				// make tentative assignment
				grid[row][col].setText(num + "");

				// return, if success, yay!
				if (solve())
					return true;

				// failure, unmake & try again
				grid[row][col].setText("");
			}
		}
		
//		// consider digits 1 to 9
//		for (int num = 1; num <= grid.length; num++) {
//			// if looks promising
//			if (isValid(row, col, num, false)) {
//				// System.out.println("Valid");
//				// make tentative assignment
//				grid[row][col].setText(num + "");
//
//				// return, if success, yay!
//				if (solve())
//					return true;
//
//				// failure, unmake & try again
//				grid[row][col].setText("");
//			}
//		}

		return false; // this triggers backtracking
	}
	
	public boolean solveOne(){
		int validDigit = 0;
		int numValid = 0;
		for (int row = 0; row < grid.length; row++) {
	        for (int col = 0; col < grid.length; col++) {
				for (int num = 1; num <= grid.length; num++){
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
						if (isValid(row, col, num, false)){
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
	
	public boolean solveSections(){
		int validIndex = 0;
		int numValid = 0;
		for (int num = 1; num <= grid.length; num++){
			for (int row = 0; row < grid.length; row++) {
				for (int col = 0; col < grid.length; col++) {			
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
				        if (isValid(row, col, num, false)){
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
			
			for (int col = 0; col < grid.length; col++) {
				for (int row = 0; row < grid.length; row++) {			
			        // if looks promising
					if (grid[row][col].getText().isEmpty()) {
				        if (isValid(row, col, num, false)){
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
			
			int boxSize = (int)Math.sqrt(grid.length);
			
			int validRow = 0;
			int validCol = 0;
					
			for (int i = 0; i < boxSize; i++){
				for (int row = 0; row < boxSize; row++){
					for (int col = 0; col < boxSize; col++){
						if (grid[row+(i*boxSize)][col+(i*boxSize)].getText().isEmpty()) {
					        if (isValid(row+(i*boxSize), col+(i*boxSize), num, false)){
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
	

	public boolean isValid(int row, int col, int digit, boolean check) {

		// Check the row and column
		for (int i = 0; i < grid.length; i++) {
			if (i != col && grid[row][i].getText().equals(digit + "")) {
				if (check)
					System.out.println("1st " + row + i);
				return false;
			}
			if (i != row && grid[i][col].getText().equals(digit + "")) {
				if (check)
					System.out.println("2nd " + i + col);
				return false;
			}
		}

		// Check the square
		int boxSize = (int) Math.sqrt(grid.length);

		int newRow = row - row % boxSize;
		int newCol = col - col % boxSize;

		for (int i = 0; i < boxSize; i++) {
			for (int j = 0; j < boxSize; j++) {
				if (!(i + newRow == row && j + newCol == col)
						&& grid[i + newRow][j + newCol].getText().equals(
								digit + "")) {
					if (check)
						System.out.println("3rd " + (i + row) + (j + col));
					return false;
				}
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
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				String value = grid[x][y].getText();
				if (!isValid(x, y, Integer.parseInt(value), true)) {
					JOptionPane.showMessageDialog(this,
							"The solution is incorrect.");
					System.out.println(x + ", " + y + ": "
							+ Integer.parseInt(value));
					return;
				}
			}
		}

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
		if (action.equals("decreaseSize")) {
			decrease();
		}
		if (action.equals("increaseSize")) {
			increase();
		}
		if (action.equals("solve")) {
			
			int timeout = 50;
			boolean success = true;
			while (findEmpty() != null){
				solveOne();
				solveSections();
				timeout--;
				if(timeout == 0)
					break;
			}
			if(findEmpty() != null){
				generatePossibilities();
				success = solve();
			}
			
			//boolean success = solve();
			if (!success) {
				JOptionPane.showMessageDialog(this,
						"There is no solution to this puzzle.");
			}
			else {
				print();
			}
		}
		if (action.equals("checkSolution")) {
			checkSolution();
		}

	}
	
	public void print(){
		int rCounter = 1;
		for (JButton[] row : grid) {
			if (rCounter > grid.length)
				rCounter = 1;
			int cCounter = 1;
			for (JButton num : row) {
				System.out.print(num.getText() + " ");
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
