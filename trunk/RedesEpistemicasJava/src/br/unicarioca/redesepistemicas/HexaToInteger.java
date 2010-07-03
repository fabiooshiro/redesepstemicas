package br.unicarioca.redesepistemicas;
import java.io.*;
import java.lang.*;

public class  HexaToInteger{
  public static void main(String[] args) throws IOException{
    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the hexadecimal value:!");
    String s = read.readLine();
    int i = Integer.valueOf(s, 16).intValue();
    System.out.println("Integer:=" + i);
  }
}