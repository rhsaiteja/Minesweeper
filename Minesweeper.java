import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
class MineFrame extends JFrame implements ActionListener,MouseListener
{
	MyButton b[][]=new MyButton[10][10];
	JLabel l[][]=new JLabel[10][10];
	Random r=new Random(new Date().getTime());
	JDialog d=new JDialog();
	JLabel lost=new JLabel("you lost",SwingConstants.CENTER);
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
		for(i=0;i<10;i++)
		{
			int a=r.nextInt(10);
			int b=r.nextInt(10);
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
			d.add(lost);
			d.setVisible(true);
			d.setSize(500,500);
			for(int lv1=0;lv1<10;lv1++)
			{
				for(int lv2=0;lv2<10;lv2++)
				{
					if(b[lv1][lv2]!=null)
						b[lv1][lv2].removeActionListener(this);
				}
			}
		}
		if(l.getText().equals("0"))
		{
			for(int p=i-1;p<=i+1;p++)
			{
				for(int q=j-1;q<=j+1;q++)
				{
					try
					{
						if(b[i][j]!=null)
							actionPerformed(new ActionEvent(b[p][q],ActionEvent.ACTION_PERFORMED,"CLICKED"));
					}catch(ArrayIndexOutOfBoundsException aie){}
				}
			}
		}
	}
	public void mouseClicked(MouseEvent me)
	{
		if(me.getButton()==MouseEvent.BUTTON3)
		{
			MyButton ab=(MyButton)me.getSource();
			MyButton bn=null;
			if(ab.getText().equals(""))
				bn=new  MyButton(ab.getConnectedLabel(),"FLAG",ab.getI(),ab.getJ());
			else if(ab.getText().equals("FLAG"))
				bn=new  MyButton(ab.getConnectedLabel(),"",ab.getI(),ab.getJ());
			bn.setBounds(ab.getBounds());
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
