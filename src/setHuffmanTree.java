import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
//定义哈夫曼树节点数据结构
class TreeNode
{
    int power;
    char key;
    String code="";
    TreeNode Left=null;
    TreeNode Right=null;
    //构造函数，初始化权值power和字符名key
    public TreeNode(int power,char key)
    {
        this.power=power;
        this.key =key;
    }
    public int codeToInt()
    {
        return Integer.parseInt(code,2);
    }
}


class Huffman {
    ArrayList<TreeNode> leafcontainer=new ArrayList<>();
    //遍历哈弗漫树，并根据此将叶子节点的哈夫曼编码写入code中，leafcontainer用于放置处理后的叶子节点
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("GBK");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public  void bianli(TreeNode rootnode)
    {

        //如果节点有子节点则左节点code末尾加0，右节点code末尾加1，然后递归，再调用一次bianli
        if(rootnode.Left!=null&&rootnode.Right!=null)
        {
            rootnode.Left.code =rootnode.code +"0";
            rootnode.Right.code =rootnode.code +"1";
            bianli(rootnode.Left);
            bianli(rootnode.Right);
        }
        //否则的话就将该叶子节点放入到leafcontainer中
        else leafcontainer.add((rootnode));
    }

    //定义比较器，比较两节点中的权值大小，按升序排列
    static Comparator<TreeNode> cmp=new Comparator<TreeNode>() {
        @Override
        public int compare(TreeNode o1, TreeNode o2) {
            return o1.power-o2.power;
        }
    };


    public ArrayList readFile(String filepath)
    {
        ArrayList<Byte> byteArrayList=new ArrayList<>();
        ArrayList<Character> zifuji=new ArrayList<>();
        String content=null;
        try {
            byte[] buffer=new byte[1];
            FileInputStream fis=new FileInputStream(filepath);
            while(fis.read(buffer)!=-1){
              byteArrayList.add(buffer[0]);
            }
            fis.close();
            byte[] b2=new byte[byteArrayList.size()];
            for(int i=0;i<byteArrayList.size();i++)
            {
                b2[i]=byteArrayList.get(i);
            }
            char[] c=getChars(b2);
            for(char x:c)
            {
                zifuji.add(x);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return zifuji;
    }


    public void set(ArrayList<Character> zifuji)
    {

        //定义一个装树节点的优先队列q
        Queue<TreeNode> priorityQueue=new PriorityQueue<>(cmp);
        //定义一个map
        Map<Character,Integer> map=new HashMap<>();
        //将zifuji中的数据传入map中
        for(char x:zifuji)
        {
            if(!map.containsKey(x))
            {
                map.put(x,1);
            }
            else
            {
                map.put(x,map.get(x)+1);
            }
        }
        //再把map中的值赋给优先队列priorityqueue
        for(char x:map.keySet())
        {
            priorityQueue.add(new TreeNode(map.get(x),x));
        }
        //建立哈夫曼树，到最后优先队列只剩一个元素时停止
        while(priorityQueue.size()>1)
        {
            //把队列中最前面的两个元素出列
            TreeNode a=priorityQueue.poll();
            TreeNode b=priorityQueue.poll();
            //二者权值相加赋给新节点c，中间节点的键值为@
            TreeNode c=new TreeNode(a.power+b.power,'@');
            c.Left=a;
            c.Right=b;
            //把新节点加入到优先队列q中
            priorityQueue.add(c);
        }
        bianli(priorityQueue.peek());
    }

    public ArrayList turnTo01(ArrayList<Character> zifuji,ArrayList<TreeNode> LeafContainer)
    {
        ArrayList<Integer> line01=new ArrayList<>();
        String buffer;
        for (char x:zifuji)
        {
            for(TreeNode t:LeafContainer)
            {
                if (x==t.key)
                {
                    buffer=t.code;
                    for(int i=0;i<buffer.length();i++)
                    {
                        if(buffer.charAt(i)=='0')
                            line01.add(0);
                        else
                            line01.add(1);
                    }
                }
            }
        }
        return line01;
    }

    public int convert01(int number)
    {
        if(number==0)
            return 1;
        else
            return 0;
    }

    public ArrayList devideGroup(ArrayList<Integer> line01)
    {
        ArrayList<Byte> byteArrayList=new ArrayList<>();
        int sign,turnbyte;
        int[] num=new int[7];
        while(line01.size()>7)
        {
            sign=line01.get(0);
            line01.remove(0);
            for(int i=6;i>=0;i--)
            {
                num[i]=line01.get(0);
                line01.remove(0);
            }
            if(sign==0)
            {
                turnbyte=64*num[6]+32*num[5]+16*num[4]+8*num[3]+4*num[2]+2*num[1]+num[0];
            }
            else
            {
                turnbyte=-(64*convert01(num[6])+32*convert01(num[5])+16*convert01(num[4])+8*convert01(num[3])
                        +4*convert01(num[2])+2*convert01(num[1])+convert01(num[0])+1);
            }
            byteArrayList.add((byte)turnbyte);
        }
        return byteArrayList;
    }


    public void writeTxtFile(String destpath,ArrayList<Byte> byteArrayList)
    {
        System.out.println("叶子个数："+leafcontainer.size());
        ArrayList<Character> codetxt=new ArrayList<>();
        for(TreeNode t:leafcontainer)
        {
            codetxt.add(t.key);
            for(int i=0;i<t.code.length();i++)
            {
                codetxt.add(t.code.charAt(i));
            }
            System.out.println(t.key+":"+t.code+"     "+t.power);
        }
        char[] c=new char[codetxt.size()];
        for(int i=0;i<codetxt.size();i++)
        {
            c[i]=codetxt.get(i);
        }
        byte[] codetxtbuffer=getBytes(c);
        try {
            FileOutputStream fos=new FileOutputStream(destpath);
            fos.write(codetxtbuffer);

            byte[] interval=new byte[5];
            for(int i=0;i<5;i++)
            {
                interval[i]=-128;
            }
            fos.write(interval);
            byte[] buffer=new byte[byteArrayList.size()];
            for(int i=0;i<byteArrayList.size();i++)
            {
                buffer[i]=byteArrayList.get(i);
            }
            fos.write(buffer);
            fos.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}



