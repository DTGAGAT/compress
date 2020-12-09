import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class iacm extends JFrame {


    public static void main(String[] args)
    {
        GraphicsEnvironment eq = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = eq.getAvailableFontFamilyNames();
        /*for(int i=0;i<fontNames.length;i++)
        {
            System.out.println(fontNames[i]);
        }*/

        JFrame frame=new JFrame("你在赣神魔");
        Container container=frame.getContentPane();
        Dimension dimension=new Dimension();
        Point point=new Point();
        ImageIcon imageIcon=new ImageIcon("C:\\Users\\28689\\Pictures\\suopin\\wjx.jpg");
        JLabel label=new JLabel("我宣你",imageIcon,JLabel.CENTER);
        Font font=new Font("楷体",Font.BOLD,30);
        label.setFont(font);
        label.setForeground(Color.blue);
        container.add(label, BorderLayout.CENTER);
        container.setBackground(Color.PINK);
        frame.setVisible(true);
        frame.setSize(320,180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
