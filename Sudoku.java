package com.raremile.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class to solve sudoku
 * 
 * @author JK
 *
 */
public class Sudoku {
	private int sudokuBoard[][];
	private int sudokuBoardSize;
	private int sudokuBoxSize;
	private boolean rowSubSet[][];
	private boolean colSubSet[][];
	private boolean boxSubSet[][];

//	private final static Logger LOGGER = Logger.getLogger(Sudoku.class
//			.getName());

	/**
	 * Overloading Sudoku class constructor to find size of the board and of
	 * each box
	 * 
	 * @param board
	 */

	public Sudoku(int board[][]) {
		sudokuBoard = board;
		sudokuBoardSize = sudokuBoard.length;
		sudokuBoxSize = (int) Math.sqrt(sudokuBoardSize);

//		LOGGER.info("The length of the board : " + sudokuBoard
//				+ ", the box size is : " + sudokuBoxSize);
	}

	/**
	 * Create subsets to solve
	 */
	public void initializeSubsets() {
		rowSubSet = new boolean[sudokuBoardSize][sudokuBoardSize];
		colSubSet = new boolean[sudokuBoardSize][sudokuBoardSize];
		boxSubSet = new boolean[sudokuBoardSize][sudokuBoardSize];
		for (int i = 0; i < sudokuBoard.length; i++) {
			for (int j = 0; j < sudokuBoard.length; j++) {
				int value = sudokuBoard[i][j];
				if (value != 0) {
					setSubsetValue(i, j, value, true);
				}
			}
		}
	}

	/**
	 * Set subset values
	 * 
	 * @param i
	 * @param j
	 * @param value
	 * @param present
	 */
	private void setSubsetValue(int i, int j, int value, boolean present) {
		rowSubSet[i][value - 1] = present;
		colSubSet[j][value - 1] = present;
		boxSubSet[computeBoxNo(i, j)][value - 1] = present;
		
//		LOGGER.info("The present value : "+present);
	}

	/**
	 * Begin Solve with initial parameter 0 , 0
	 * 
	 * @return
	 */
	public boolean solve() {
//		LOGGER.info("Calling the solve function with parameters");
		return solve(0, 0);
	}

	/**
	 * Recursive function [when the value of the cell is !0]
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean solve(int i, int j) {
		if (i == sudokuBoardSize) {
			i = 0;
			if (++j == sudokuBoardSize) {
				return true;
			}
		}
		if (sudokuBoard[i][j] != 0) {
			return solve(i + 1, j);
		}
		for (int value = 1; value <= sudokuBoardSize; value++) {
			if (isValid(i, j, value)) {
				sudokuBoard[i][j] = value;
				setSubsetValue(i, j, value, true);
				if (solve(i + 1, j)) {
//					LOGGER.info("Solve(int i, int j) RECURSIVELY called!");
					return true;
				}
				setSubsetValue(i, j, value, false);
			}
		}

		sudokuBoard[i][j] = 0;
//		LOGGER.info("Solve(int i, int j) called!");
		return false;
	}

	/**
	 * Check if value is valid
	 * 
	 * @param i
	 * @param j
	 * @param valz
	 * @return
	 */
	private boolean isValid(int i, int j, int valz) {
		valz--;
		boolean isPresent = rowSubSet[i][valz] || colSubSet[j][valz]
				|| boxSubSet[computeBoxNo(i, j)][valz];
//		LOGGER.info("Performing validation");
		return !isPresent;
	}

	/**
	 * Compute box numbr
	 * 
	 * @param i
	 * @param j
	 * @return
	 */

	private int computeBoxNo(int i, int j) {
		int boxRow = i / sudokuBoxSize;
		int boxCol = j / sudokuBoxSize;
//		LOGGER.info("Comuting box, ROW : "+boxRow+" COLUMN : "+boxCol);
		return boxRow * sudokuBoxSize + boxCol;
	}

	/**
	 * Print sudoku values
	 */

	public void print() {
		for (int i = 0; i < sudokuBoardSize; i++) {
			if (i % sudokuBoxSize == 0) {
				System.out.println(" -----------------------");
			}
			for (int j = 0; j < sudokuBoardSize; j++) {
				if (j % sudokuBoxSize == 0) {
					System.out.print("| ");
				}
				System.out.print(sudokuBoard[i][j] != 0 ? ((Object) (Integer
						.valueOf(sudokuBoard[i][j]))) : " ");
				System.out.print(' ');
			}

			System.out.println("|");
		}

		System.out.println(" -----------------------");
	}

	/**
	 * Main method to create objects and function calling, board.txt contains
	 * the sudoku problem
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		int[][] board = new int[9][9];
		File file = new File("files/board.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String sudoLine;
			int x = 0;
			while ((sudoLine = br.readLine()) != null) {
				for (int i = 0; i < 9; i++) {
					board[x][i] = sudoLine.charAt(i) - 48;
				}
				x++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sudoku sudoku = new Sudoku(board);
		sudoku.initializeSubsets();
		sudoku.solve();
		sudoku.print();

	}
}
