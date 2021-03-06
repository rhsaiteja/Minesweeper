import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
class MineFrame extends JFrame implements ActionListener,MouseListener
{
	int num_flags = 0;
	int num_mines = 10;
	int open_cells = 0;
	MyButton b[][]=new MyButton[10][10];
	JLabel l[][]=new JLabel[10][10];
	Random r=new Random(new Date().getTime());
	JDialog d=new JDialog();
	JLabel msg=new JLabel("",SwingConstants.CENTER);
	MineFrame()
	{
		super("Minesweeper");	
	}
	void init()
	{
		int i,j;
		setVisible(true);
		setSize(1000,1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(10,10));
		for(i=0;i<10;i++)
		{
			for(j=0;j<10;j++)
			{
				l[i][j]=new JLabel();
				l[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				b[i][j]=new MyButton(l[i][j],"",i,j);
				b[i][j].addActionListener(this);
				b[i][j].addMouseListener(this);
				add(b[i][j]);
			}
		}
		for(i=0;i<num_mines;i++)
		{
			int a=r.nextInt(10);
			int b=r.nextInt(10);
			if(l[a][b].getText().equals("M"))
			{
				i--;
				continue;
			}
			l[a][b].setText("M");
		}
		for(i=0;i<10;i++)
		{
			for(j=0;j<10;j++)
			{
				if(l[i][j].getText().equals("M"))
					continue;
				int count=0;
				for(int p=i-1;p<=i+1;p++)
				{
					for(int q=j-1;q<=j+1;q++)
					{
						try
						{
							if(l[p][q].getText().equals("M"))
								count++;
						}catch(ArrayIndexOutOfBoundsException aie){}
					}
				}
				l[i][j].setText(String.valueOf(count));
			}
		}
	}
	public static void main(String args[])
	{
		MineFrame mf1=new MineFrame();
		mf1.init();
	}
	public void actionPerformed(ActionEvent ae)
	{
		open_cells++;
		MyButton ab=(MyButton)ae.getSource();
		JLabel l=ab.getConnectedLabel();
		add(l);
		l.setBounds(ab.getBounds());
		remove(ab);
		int i=ab.getI(); int j=ab.getJ();
		b[i][j]=null;
		if(l.getText().equals("M"))
		{
			//stops the game
			l.setOpaque(true);
			l.setBackground(Color.red);
			msg.setText("YOU LOST THE GAME");
			d.add(msg);
			JButton newgame = new JButton("New Game");
			newgame.addActionListener(new NewGame(this));
			d.add(newgame,BorderLayout.SOUTH);
			d.setVisible(true);
			d.setSize(500,500);
			for(int lv1=0;lv1<10;lv1++)
			{
				for(int lv2=0;lv2<10;lv2++)
				{
					if(b[lv1][lv2]!=null)
					{
						
						JLabel cl = b[lv1][lv2].getConnectedLabel();
						if(b[lv1][lv2].getText().equals("FLAG") && cl.getText().equals("M"))
						{
							b[lv1][lv2].setOpaque(true);
							//b[lv1][lv2].setContentAreaFilled(false);
							b[lv1][lv2].setBackground(Color.green);
						}
						else if(b[lv1][lv2].getText().equals("FLAG") && !cl.getText().equals("M"))
						{
							b[lv1][lv2].setOpaque(true);
							//b[lv1][lv2].setContentAreaFilled(false);
							b[lv1][lv2].setBackground(Color.red);
						}
						else if(cl.getText().equals("M")){
							add(cl);
							cl.setBounds(b[lv1][lv2].getBounds());
							remove(b[lv1][lv2]);
							b[lv1][lv2]=null;
						}
						else
							b[lv1][lv2].removeActionListener(this);
					}
				}
			}
			return;
		}
		if(l.getText().equals("0"))
		{
			for(int p=i-1;p<=i+1;p++)
			{
				for(int q=j-1;q<=j+1;q++)
				{
					try
					{
						if(b[p][q]!=null && !b[p][q].getText().equals("FLAG"))
							actionPerformed(new ActionEvent(b[p][q],ActionEvent.ACTION_PERFORMED,"CLICKED"));
					}catch(ArrayIndexOutOfBoundsException aie){}
				}
			}
		}
		if(open_cells == 100-num_mines && num_flags == num_mines)
		{
			//player won
			msg.setText("YOU WON THE GAME");
			d.add(msg);
			JButton newgame = new JButton("New Game");
			newgame.addActionListener(new NewGame(this));
			d.add(newgame,BorderLayout.SOUTH);
			d.setVisible(true);
			d.setSize(500,500);
		}
	}
	public void mouseClicked(MouseEvent me)
	{
		if(me.getButton()==MouseEvent.BUTTON3)
		{
			MyButton ab=(MyButton)me.getSource();
			MyButton bn=null;
			if(ab.getText().equals(""))
			{
				
				bn=new MyButton(ab.getConnectedLabel(),"FLAG",ab.getI(),ab.getJ());
				num_flags++;
				if(open_cells == 100-num_mines && num_flags == num_mines)
				{
					//player won
					msg.setText("YOU WON THE GAME");
					d.add(msg);
					JButton newgame = new JButton("New Game");
					newgame.addActionListener(new NewGame(this));
					d.add(newgame,BorderLayout.SOUTH);
					d.setVisible(true);
					d.setSize(500,500);
				}
			}
			else if(ab.getText().equals("FLAG"))
			{
				bn=new MyButton(ab.getConnectedLabel(),"",ab.getI(),ab.getJ());
				bn.addActionListener(this);
				num_flags--;
			}
			b[ab.getI()][ab.getJ()] = bn;
			bn.addMouseListener(this);
			add(bn);
			bn.setBounds(ab.getBounds());
			remove(ab);
		}
	}
	public void mousePressed(MouseEvent me)
	{}
	public void mouseReleased(MouseEvent me)
	{}
	public void mouseEntered(MouseEvent me)
	{}
	public void mouseExited(MouseEvent me)
	{}
}
class MyButton extends JButton
{
	JLabel cl;
	int i,j;
	MyButton(JLabel l,String name,int i,int j)
	{
		super(name);
		cl=l;
		this.i=i; this.j=j;
	}
	JLabel getConnectedLabel()
	{
		return cl;
	}
	int getI()
	{
		return i;
	}
	int getJ()
	{
		return j;
	}
}
class NewGame implements ActionListener
{
	JFrame oldgame;
	NewGame(JFrame oldgame)
	{
		this.oldgame = oldgame;
	}
	public void actionPerformed(ActionEvent ngevent)
	{
		oldgame.dispose();
		new MineFrame().init();
	}
}
