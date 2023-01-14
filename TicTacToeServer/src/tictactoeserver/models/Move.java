package tictactoeserver.models;

public class Move {

    private int row;
    private int column;
    private char symbol;

    public Move() {
    }

    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Move(int row, int column, char symbol) {
        this.row = row;
        this.column = column;
        this.symbol = symbol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "{" + symbol + ", " + row + "," + column + "}";
    }

}
