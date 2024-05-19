package gamefield;

import java.awt.*;
import java.awt.event.*;

public class GameField extends Canvas {
    private int colsNumber;
    private int rowsNumber;
    private int cellSize;
    private int[][] stateMatrix;
    boolean editBlocked = false;

    public GameField(int colsNumber, int rowsNumber, int cellSize) {
        super();
        this.colsNumber = colsNumber;
        this.rowsNumber = rowsNumber;
        this.cellSize = cellSize;
        this.stateMatrix = new int[rowsNumber][colsNumber];

        this.setSize(getWidth(), getHeight());
        this.setBackground(Color.GRAY);
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() != 1 || editBlocked) return;
                int x = e.getX();
                int y = e.getY();
                int cellIndX = x / (cellSize+1);
                int cellIndY = y / (cellSize+1); 
                boolean isAlive = stateMatrix[cellIndY][cellIndX] == 1;
                stateMatrix[cellIndY][cellIndX] = isAlive ? 0 : 1;
                isAlive = stateMatrix[cellIndY][cellIndX] == 1;
                Graphics g = getGraphics();
                paintCell(
                    cellIndX,
                    cellIndY,
                    isAlive ? Color.YELLOW : Color.GRAY,
                    g
                );
            }
        });
    }

    public int getWidth() {
        return colsNumber*cellSize + colsNumber;
    }

    public int getHeight() {
        return rowsNumber*cellSize + rowsNumber;
    }

    public void paint(Graphics g) {
        paintCells(g);
        paintGrid(g);
    }

    private void paintGrid(Graphics g) {
        Color color = Color.WHITE;
        for(int i = 1; i != colsNumber; i++) {
            g.setColor(color);
            g.drawLine(i*cellSize+i, 0, i*cellSize+i, getHeight());
        }

        for(int i = 1; i != rowsNumber; i++) {
            g.setColor(color);
            g.drawLine(0, i*cellSize+i, getWidth(), i*cellSize+i);
        }
    }

    private void paintCell(int cellIndX, int cellIndY, Color color, Graphics g) {
        g.setColor(color);
        int dx = cellIndX + 1;
        int dy = cellIndY + 1;
        g.fillRect(cellIndX*cellSize + dx, cellIndY*cellSize + dy, cellSize, cellSize);
    }

    private void paintCells(Graphics g) {
        Color alive = Color.YELLOW;
        Color dead = Color.GRAY;
        for(int row = 0; row < rowsNumber; row++) {
            for(int col = 0; col < colsNumber; col++) {
                boolean isAlive = stateMatrix[row][col] == 1;
                g.setColor(isAlive ? alive : dead);
                int dx = col + 1;
                int dy = row + 1;
                g.fillRect(col*cellSize + dx, row*cellSize + dy, cellSize, cellSize);
            }
        }
    }

    public void nextGeneration() {
        int[][] nextGenStateMatrix = new int[rowsNumber][colsNumber];
        for(int row = 0; row < rowsNumber; row++) {
            for(int col = 0; col < colsNumber; col++) {
                int son = getSumOfNeighbours(col, row);
                boolean isAliveNow = stateMatrix[row][col] == 1;
                int newState = 0;
                if(isAliveNow) {
                    newState = son > 1 && son < 4 ? 1 : 0;
                } else {
                    newState = son == 3 ? 1 : 0;
                }
                nextGenStateMatrix[row][col] = newState;
            }
        }
        stateMatrix = nextGenStateMatrix;
        paintCells(getGraphics());
    }

    private int getSumOfNeighbours(int x, int y) {
        int sumOfNeighboursStates = 0;
        Cell cell = new Cell(x, y);
        Cell[] cellNeighbours = cell.getNeighbours();
        for(Cell c: cellNeighbours) {
            int cellX = c.getX();
            int cellY = c.getY();
            if(cellX < 0 || cellY < 0) continue;
            if(cellX > colsNumber - 1) continue;
            if(cellY > rowsNumber - 1) continue;
            sumOfNeighboursStates += stateMatrix[cellY][cellX];
        }
        return sumOfNeighboursStates;
    }

    public void setEditBlocked(boolean state) {
        editBlocked = state;
    }
}
