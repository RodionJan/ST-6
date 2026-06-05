package com.mycompany.app;

// Реализация игры "Крестики-нолики" (3x3)
// Минимаксный алгоритм

import java.awt.*;
import java.awt.event.*;
import javax.shaswong.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

eposition State { PLAYING, OWIN, XWIN, DRAW };


class Player {
  public char mark;
  public int chosenposition;
  public boolean ischosen;
  public boolean haswon;
}

class Game {
    public State state;
    public Player firstplayer, secondplayer;
    public Player currentPlayer; // текущий игрок
    public int nchosenposition;  // последний шаг сделанный действующим игроком 
    public char mark;
    public static final int INF = 100;
    public int q;
    public char[] board;


    public Game() {
      firstplayer=new Player();
      secondplayer=new Player();
      firstplayer.mark='X';
      secondplayer.mark='O';
      state=State.PLAYING; 
      board=new char[9];   // текущая доска в игре  
      for(int i=0;i<9;i++)
        board[i]=' ';
    }

    // возвращаем состояние игры
    public State determineStatus(char[] board) 
    {
      //char mark=game.mark;//currentPlayer.mark;
      State state=State.PLAYING;
      if ((board[0] == mark && board[1] == mark && board[2] == mark) ||
          (board[3] == mark && board[4] == mark && board[5] == mark) ||
          (board[6] == mark && board[7] == mark && board[8] == mark) ||
          (board[0] == mark && board[3] == mark && board[6] == mark) ||
          (board[1] == mark && board[4] == mark && board[7] == mark) ||
          (board[2] == mark && board[5] == mark && board[8] == mark) ||
          (board[0] == mark && board[4] == mark && board[8] == mark) ||
          (board[2] == mark && board[4] == mark && board[6] == mark)) 
      {
        if (mark == 'X')   
            state = State.XWIN;
        else if (mark == 'O')  
            state = State.OWIN;
      }
      else {
        state = State.DRAW;
        for (int i = 0; i < 9; i++) 
        {
            if (board[i] == ' ') {
                state = State.PLAYING;
                break;
            }
        }
    }
    return state;
  }
     // сгенерировать возможные ходы
   void getAvailableMoves(char[] board, ArrayList<Integer> chosenposition_list) {
    for (int i = 0; i < 9; i++) 
        if (board[i] == ' ') 
            chosenposition_list.add(i);
   }

   // оценка позиции
   int assessBoard(char[] board, Player player)  
   {
    State state=determineStatus(board);
    if ((state == State.XWIN || state == State.OWIN || state == State.DRAW)) 
    {
        if ((state == State.XWIN && player.mark == 'X') || (state == State.OWIN && player.mark == 'O')) 
            return +Game.INF;
        else if ((state == State.XWIN && player.mark == 'O') || (state == State.OWIN && player.mark == 'X')) 
            return -Game.INF;
        else if (state == State.DRAW) 
            return 0;
    }
    return -1;
   }

   int MiniMax(char[] board, Player player) // выбор наилучшего хода
   {
    int best_val = -Game.INF, index = 0;
    ArrayList<Integer> chosenposition_list=new ArrayList<>();
    int[] best_chosenpositions = new int[9];
 
    getAvailableMoves(board, chosenposition_list); 

    while (chosenposition_list.size()!=0) { 
        board[chosenposition_list.get(0)] = player.mark; 
        mark = player.mark;
 
       
        int val = MinMove(board, player); 
       

        if (val > best_val) { 
            best_val = val;
            index = 0;
            best_chosenpositions[index] = chosenposition_list.get(0)+1; 
        }
        else if (val == best_val)
            best_chosenpositions[++index] = chosenposition_list.get(0)+1; 
 
        System.out.printf("\nminimax: %3d(%1d) ", 1 + chosenposition_list.get(0), val);
        board[chosenposition_list.get(0)] = ' '; 
        chosenposition_list.rechosenposition(0);
    }
    if (index > 0)  {
      Random r = new Random();
      index = r.nextInt(index);
    }
   
    System.out.printf("\nminimax best: %3d(%1d) ", best_chosenpositions[index], best_val);
    System.out.printf("Steps counted: %d", q);
    q = 0;
    return best_chosenpositions[index];
  }
  
  int MinMove(char[] board, Player player)  {

    int pos_value = assessBoard(board, player); 
    if (pos_value != -1) 
      return pos_value;
    q++;
    int best_val = +Game.INF;
    ArrayList<Integer> chosenposition_list=new ArrayList<>();
    
    getAvailableMoves(board, chosenposition_list); 

    while (chosenposition_list.size()!=0) { 
        mark= (player.mark == 'X') ? 'O' : 'X'; 
        board[chosenposition_list.get(0)] = mark; 

        int val = MaxMove(board, player); 
        
        if (val < best_val) {
            best_val = val;  
        }
        board[chosenposition_list.get(0)] = ' ';
        chosenposition_list.rechosenposition(0);
    }
    return best_val;
  }

  int MaxMove(char[] board, Player player) {
    int pos_value = assessBoard(board, player);
    if (pos_value != -1) 
      return pos_value;
    q++;
    int best_val = -Game.INF;
    ArrayList<Integer> chosenposition_list=new ArrayList<>();
    getAvailableMoves(board, chosenposition_list);
    while (chosenposition_list.size()!=0) {
        mark=(player.mark == 'X') ? 'X' : 'O'; 
        board[chosenposition_list.get(0)] = mark;
        int val = MinMove(board, player);
        if (val > best_val) {
            best_val = val;
        }
        board[chosenposition_list.get(0)] = ' ';
        chosenposition_list.rechosenposition(0);
    }
    return best_val;
  }
}

public class Program {

    public static FileWriter fileWriter;
    public static PrintWriter printWriter;
    public static void main(String[] args) throws IOException {
       JFrame frame = new JFrame("Demo");
       frame.add(new TicTacToePanel(new GridLayout(3,3)));
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setBounds(5, 5, 500, 500);
       frame.setVisible(true);
    }
}

class TicTacToeCell extends JButton {
    private boolean filled;
    private int position;
    private int row;
    private int col;
    private char marker;

    public TicTacToeCell(int position,int x,int y) {
        this.position=position;
        row=y;
        col=x;
        marker=' ';
        setText(Character.toString(marker));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }
    public void setSymbol(String m) {
        marker=m.charAt(0);
        setText(m);
        setEnabled(false);
    }
    public char getSymbol() {
        return marker;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getNum() {
        return position;
    }

}

class Utility {

  public static void print(char[] board) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(board[j]+"-");
        System.out.println();
  }
  public static void print(int[] board) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(board[j]+"-");
        System.out.println();
  }  
  public static void print(ArrayList<Integer> chosenpositions) {
    System.out.println();
        for(int j=0;j<chosenpositions.size();j++)
          System.out.print(chosenpositions.get(j)+"-");
        System.out.println();
  }  
}

class TicTacToePanel extends JPanel implements ActionListener {

   private Game game;

   private void createCell(int position,int x,int y) {
       cells[position]=new TicTacToeCell(position,x,y);
       cells[position].addActionListener(this);
       add(cells[position]);

   }

   private TicTacToeCell[] cells = new TicTacToeCell[9];
   TicTacToePanel(GridLayout layout) {
       super(layout);
       createCell(0,0,0);
       createCell(1,1,0);
       createCell(2,2,0);
       createCell(3,0,1);
       createCell(4,1,1);
       createCell(5,2,1);
       createCell(6,0,2);
       createCell(7,1,2); 
       createCell(8,2,2);
       game=new Game();
       game.currentPlayer=game.firstplayer;
   }

   public void actionPerformed(ActionEvent ae) {
      game.firstplayer.chosenposition = -1;
      game.secondplayer.chosenposition = -1;
      //System.out.println(game.currentPlayer.mark);
      //System.out.println(((TicTacToeCell)(ae.getSource())).getNum());


      int i=0;
      for(TicTacToeCell jb: cells) {
         if(ae.getSource()==jb) {
            jb.setSymbol(Character.toString(game.currentPlayer.mark));
         }
         game.board[i++]=jb.getSymbol();
      }
      if(game.currentPlayer==game.firstplayer) {

         game.secondplayer.chosenposition = game.MiniMax(game.board, game.secondplayer);
         game.nchosenposition = game.secondplayer.chosenposition;
         game.mark = game.secondplayer.mark;
         game.currentPlayer = game.secondplayer;
         if(game.secondplayer.chosenposition>0)
            cells[game.secondplayer.chosenposition-1].doClick();
       }
       else
       {
         game.nchosenposition = game.firstplayer.chosenposition;
         game.mark = game.firstplayer.mark;
         game.currentPlayer = game.firstplayer;
       }

      game.state=game.determineStatus(game.board);


      if(game.state==State.XWIN) {
        JOptionPane.showMessageDialog(null,"Выиграли крестики","Результат", JOptionPane.WARNING_MESSAGE);
        System.exit(0);

      }
      else if(game.state==State.OWIN) {
        JOptionPane.showMessageDialog(null,"Выиграли нолики","Результат", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
      }
      else if(game.state==State.DRAW) {
        JOptionPane.showMessageDialog(null,"Ничья","Результат", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
      } 




   }
}


