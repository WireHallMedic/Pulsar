package Pulsar.Zone;

import java.util.*;

public abstract class MapTemplate
{
	protected char[][] cellMap;
   protected int width;
   protected int height;


	public char[][] getCellMap(){return cellMap;}
   public char getCell(int x, int y){return cellMap[x][y];}
   public int getWidth(){return width;}
   public int getHeight(){return height;}

	public void setCellMap(char[][] c){cellMap = c;}
   public void setCell(int x, int y, char c){cellMap[x][y] = c;}
   
   public MapTemplate(Vector<String> input)
   {
      width = 0;
      height = 0;
      cellMap = null;
      
      if(input != null || input.size() > 0)
      {
         set(input);
      }
   }
   
   public void set(Vector<String> input)
   {
      width = input.elementAt(0).length();
      height = input.size();
      cellMap = new char[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         cellMap[x][y] = input.elementAt(y).charAt(x);
   }
   
   public void print()
   {
      for(int y = 0; y < height; y++)
      {
         for(int x = 0; x < width; x++)
            System.out.print("" + cellMap[x][y]);
         System.out.println("");
      }
   }

}