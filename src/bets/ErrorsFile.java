package bets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
///////////////////////////////// de sters contentul fisierului de fiecare data cand se intra din aplicatie
public class ErrorsFile {

    String fileName;
    String stringToOutput;

    public ErrorsFile() {

    }

    public ErrorsFile(String fileName, String stringToOutput) {
        this.stringToOutput = stringToOutput;
        this.stringToOutput = stringToOutput;
    }

    public void writeInFileString(String fileName, String stringToOutput) {
        try {
            FileWriter fstream = new FileWriter(fileName, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(stringToOutput);
            out.close();
        } catch (Exception e) {
            System.err.println("Error while writing to file: "
                    + e.getMessage());
        }
    }

    public void writeInFileString() {
        try {
            FileWriter fstream = new FileWriter(this.stringToOutput, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(this.stringToOutput);
            out.close();
        } catch (Exception e) {
            System.err.println("Error while writing to file: "
                    + e.getMessage());
        }
    }

    public String convertToString(int info) {
        StringBuilder infoSB = new StringBuilder();
        infoSB.append(info);
        return infoSB.toString();
    }

    public String convertToString(float info) {
        StringBuilder infoSB = new StringBuilder();
        infoSB.append(info);
        return infoSB.toString();
    }

    public String convertToString(Exception info) {
        StringBuilder infoSB = new StringBuilder();
        infoSB.append(info);
        return infoSB.toString();
    }

    public String convertToString(IOException info) {
        StringBuilder infoSB = new StringBuilder();
        infoSB.append(info);
        return infoSB.toString();
    }

    public static void main(String args[]) {
        new ErrorsFile();
    }
}
