// Реализация игры "Крестики-нолики" (3x3)
// Минимаксный алгоритм

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

enum State { PLAYING, OWIN, XWIN, DRAW };


class Player {
  public char mark;
  public int move;
  public boolean isPicked;
  public boolean victory;
}

class Game {
    public State state;
    public Player player1, player2;
    public Player cplayer; // текущий игрок
    public int nmove;  // последний шаг сделанный действующим игроком 
    public char mark;
    public static final int INF = 100;
    public int q;
    public char[] grid;


    public Game() {
      player1=new Player();
      player2=new Player();
      player1.mark='X';
      player2.mark='O';
      state=State.PLAYING; 
      grid=new char[9];   // текущая доска в игре  
      for(int i=0;i<9;i++)
        grid[i]=' ';
    }

    // возвращаем состояние игры
    public State determineStatus(char[] grid) 
    {
      //char mark=game.mark;//cplayer.mark;
      State state=State.PLAYING;
      if ((grid[0] == mark && grid[1] == mark && grid[2] == mark) ||
          (grid[3] == mark && grid[4] == mark && grid[5] == mark) ||
          (grid[6] == mark && grid[7] == mark && grid[8] == mark) ||
          (grid[0] == mark && grid[3] == mark && grid[6] == mark) ||
          (grid[1] == mark && grid[4] == mark && grid[7] == mark) ||
          (grid[2] == mark && grid[5] == mark && grid[8] == mark) ||
          (grid[0] == mark && grid[4] == mark && grid[8] == mark) ||
          (grid[2] == mark && grid[4] == mark && grid[6] == mark)) 
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
            if (grid[i] == ' ') {
                state = State.PLAYING;
                break;
            }
        }
    }
    return state;
  }
     // сгенерировать возможные ходы
   void generateMoves(char[] grid, ArrayList<Integer> move_list) {
    for (int i = 0; i < 9; i++) 
        if (grid[i] == ' ') 
            move_list.add(i);
   }

   // оценка позиции
   int evaluatePosition(char[] grid, Player player)  
   {
    State state=determineStatus(grid);
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

   int MiniMax(char[] grid, Player player) // выбор наилучшего хода
   {
    int best_val = -Game.INF, index = 0;
    ArrayList<Integer> move_list=new ArrayList<>();
    int[] best_moves = new int[9];
 
    generateMoves(grid, move_list); 

    while (move_list.size()!=0) { 
        grid[move_list.get(0)] = player.mark; 
        mark = player.mark;
 
       
        int val = MinMove(grid, player); 
       

        if (val > best_val) { 
            best_val = val;
            index = 0;
            best_moves[index] = move_list.get(0)+1; 
        }
        else if (val == best_val)
            best_moves[++index] = move_list.get(0)+1; 
 
        System.out.printf("\nminimax: %3d(%1d) ", 1 + move_list.get(0), val);
        grid[move_list.get(0)] = ' '; 
        move_list.remove(0);
    }
    if (index > 0)  {
      Random r = new Random();
      index = r.nextInt(index);
    }
   
    System.out.printf("\nminimax best: %3d(%1d) ", best_moves[index], best_val);
    System.out.printf("Steps counted: %d", q);
    q = 0;
    return best_moves[index];
  }
  
  int MinMove(char[] grid, Player player)  {

    int pos_value = evaluatePosition(grid, player); 
    if (pos_value != -1) 
      return pos_value;
    q++;
    int best_val = +Game.INF;
    ArrayList<Integer> move_list=new ArrayList<>();
    
    generateMoves(grid, move_list); 

    while (move_list.size()!=0) { 
        mark= (player.mark == 'X') ? 'O' : 'X'; 
        grid[move_list.get(0)] = mark; 

        int val = MaxMove(grid, player); 
        
        if (val < best_val) {
            best_val = val;  
        }
        grid[move_list.get(0)] = ' ';
        move_list.remove(0);
    }
    return best_val;
  }

  int MaxMove(char[] grid, Player player) {
    int pos_value = evaluatePosition(grid, player);
    if (pos_value != -1) 
      return pos_value;
    q++;
    int best_val = -Game.INF;
    ArrayList<Integer> move_list=new ArrayList<>();
    generateMoves(grid, move_list);
    while (move_list.size()!=0) {
        mark=(player.mark == 'X') ? 'X' : 'O'; 
        grid[move_list.get(0)] = mark;
        int val = MinMove(grid, player);
        if (val > best_val) {
            best_val = val;
        }
        grid[move_list.get(0)] = ' ';
        move_list.remove(0);
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
    private boolean isFill;
    private int num;
    private int row;
    private int col;
    private char marker;

    public TicTacToeCell(int num,int x,int y) {
        this.num=num;
        row=y;
        col=x;
        marker=' ';
        setText(Character.toString(marker));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }
    public void setMarker(String m) {
        marker=m.charAt(0);
        setText(m);
        setEnabled(false);
    }
    public char getMarker() {
        return marker;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getNum() {
        return num;
    }

}

class Utility {

  public static void print(char[] grid) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(grid[j]+"-");
        System.out.println();
  }
  public static void print(int[] grid) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(grid[j]+"-");
        System.out.println();
  }  
  public static void print(ArrayList<Integer> moves) {
    System.out.println();
        for(int j=0;j<moves.size();j++)
          System.out.print(moves.get(j)+"-");
        System.out.println();
  }  
}

class TicTacToePanel extends JPanel implements ActionListener {

   private Game game;

   private void createCell(int num,int x,int y) {
       cells[num]=new TicTacToeCell(num,x,y);
       cells[num].addActionListener(this);
       add(cells[num]);

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
       game.cplayer=game.player1;
   }

   public void actionPerformed(ActionEvent ae) {
      game.player1.move = -1;
      game.player2.move = -1;
      //System.out.println(game.cplayer.mark);
      //System.out.println(((TicTacToeCell)(ae.getSource())).getNum());


      int i=0;
      for(TicTacToeCell jb: cells) {
         if(ae.getSource()==jb) {
            jb.setMarker(Character.toString(game.cplayer.mark));
         }
         game.grid[i++]=jb.getMarker();
      }
      if(game.cplayer==game.player1) {

         game.player2.move = game.MiniMax(game.grid, game.player2);
         game.nmove = game.player2.move;
         game.mark = game.player2.mark;
         game.cplayer = game.player2;
         if(game.player2.move>0)
            cells[game.player2.move-1].doClick();
       }
       else
       {
         game.nmove = game.player1.move;
         game.mark = game.player1.mark;
         game.cplayer = game.player1;
       }

      game.state=game.determineStatus(game.grid);


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


