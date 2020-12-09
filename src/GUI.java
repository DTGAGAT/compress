import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class example extends JFrame
{

    JPanel kapian1=new JPanel();
    JPanel kapian2=new JPanel();
    JPanel kapian3=new JPanel();
    JPanel flowpanel1=new JPanel();
    JPanel flowpanel2=new JPanel();
    JPanel flowpanel3=new JPanel();
    JPanel flowpanel4=new JPanel();
    JPanel cards=new JPanel(new CardLayout());
    CardLayout cardLayout=(CardLayout)(cards.getLayout());
    JTextField textField=new JTextField("文件路径",40);
    JTextField textField2=new JTextField("文件路径",40);
    JButton yasuo=new JButton("压缩");
    JButton jieya=new JButton("解压");
    JButton liulan=new JButton("浏览");
    JButton liulan2=new JButton("浏览");
    JButton fanhui=new JButton("返回");
    JButton yasuo2=new JButton("开始压缩");
    JButton jieya2=new JButton("开始解压");
    JButton fanhui2=new JButton("返回");

    public void changeCard(JButton button, String cardname)
    {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,cardname);
            }
        });
    }
    public void visitfile(JButton button,JTextField tf)
    {
        ActionListener actionListener=new ActionListener()
        {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(new Button());        //是否打开文件选择框
                System.out.println("returnVal="+returnVal);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filepath;//如果符合文件类型
                    filepath = chooser.getSelectedFile().getAbsolutePath();//获取绝对路径
                    System.out.println(filepath);
                    tf.setText(filepath);
                }

            }
        };
        button.addActionListener(actionListener);
    }
    public void startCompress(JButton button)
    {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Huffman Hu=new Huffman();
                try{
                    String filepath=textField.getText();
                    String destpath=filepath.substring(0,filepath.indexOf('.')+1)+"mwz";
                    ArrayList<Character> zifuji=Hu.readFile(filepath);
                    Hu.set(zifuji);
                    ArrayList<Integer> line01=Hu.turnTo01(zifuji,Hu.leafcontainer);
                    ArrayList<Byte> byteArrayList= Hu.devideGroup(line01);
                    Hu.writeTxtFile(destpath,byteArrayList);
                    JOptionPane.showMessageDialog(cards,"完美压缩，恭喜你！");
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(cards,"不支持该文件格式压缩！");
                    e1.printStackTrace();
                }
            }
        });
    }

    public void startDecompress(JButton button)
    {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deCompression Dc=new deCompression();
                try
                {
                    String filepath=textField2.getText();
                    String destpath=filepath.substring(0,filepath.indexOf('.')+1)+"txt";
                    Dc.readFile(filepath);
                    Map<String,Character> huffcode=Dc.makeCodeTable(Dc.codebox);
                    ArrayList<Character> characterArrayList=Dc.turnTo01(Dc.databox);
                    ArrayList<Character> primitiveText=Dc.translation(characterArrayList,huffcode);
                    System.out.println(characterArrayList);
                    System.out.println(huffcode);
                    Dc.writefile(destpath,primitiveText);
                    JOptionPane.showMessageDialog(cards,"完美解压，恭喜你！");
                }catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(cards,"该文件格式不能解压！");
                    e2.printStackTrace();
                }
            }
        });
    }

    public example()
    {
        textField.setEditable(true);
        textField2.setEditable(true);
        kapian1.add(yasuo);
        kapian1.add(jieya);
        flowpanel1.add(textField);
        flowpanel1.add(liulan);
        flowpanel2.add(yasuo2);
        flowpanel2.add(fanhui);
        flowpanel3.add(textField2);
        flowpanel3.add(liulan2);
        flowpanel4.add(jieya2);
        flowpanel4.add(fanhui2);
        kapian2.setLayout(new BorderLayout());
        kapian2.add(flowpanel1,BorderLayout.NORTH);
        kapian2.add(flowpanel2,BorderLayout.SOUTH);
        kapian3.setLayout(new BorderLayout());
        kapian3.add(flowpanel3,BorderLayout.NORTH);
        kapian3.add(flowpanel4,BorderLayout.SOUTH);
        cards.add(kapian1,"卡片1");
        cards.add(kapian2,"卡片2");
        cards.add(kapian3,"卡片3");
        changeCard(yasuo,"卡片2");
        changeCard(jieya,"卡片3");
        changeCard(fanhui,"卡片1");
        changeCard(fanhui2,"卡片1");
        visitfile(liulan,textField);
        visitfile(liulan2,textField2);
        startCompress(yasuo2);
        startDecompress(jieya2);
        cardLayout.show(cards,"卡片1");

        this.setSize(600,300);
        this.add(cards);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Amazip");
        this.setVisible(true);
        this.setLocation(50,50);
    }
}

public class GUI
{
    public static void main(String args[])
    {
        new example();
    }
}