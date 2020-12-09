import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class deCompression {

    ArrayList<Byte> codebox=new ArrayList<>();
    ArrayList<Byte> databox=new ArrayList<>();

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

    public  void readFile(String filepath)
    {

        int stopsign=0;
        byte[] bytesbuffer=new byte[1];
        try
        {
            FileInputStream fis=new FileInputStream(filepath);
            while (fis.read(bytesbuffer)!=-1)
            {
                if(stopsign<5)
                {
                    codebox.add(bytesbuffer[0]);
                    if(bytesbuffer[0]==-128)
                        stopsign++;
                }
                else
                    databox.add(bytesbuffer[0]);
            }
            fis.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public char convert01(char c)
    {
        if(c=='0')
        return '1';
        else
            return '0';
    }

    public ArrayList turnTo01(ArrayList<Byte> bytebox)
    {
        ArrayList<Character> characterArrayList=new ArrayList<>();
        int temp;
        String tempString;
        String convertString;
        for(byte b:bytebox)
        {
            tempString="";
            convertString="";
            temp=(int)b;
            if(temp>0)
            {
                tempString=Integer.toBinaryString(temp);
                if(tempString.length()<8);
                {
                    for(int i=0;i<8-tempString.length();i++)
                    {
                        tempString="0"+tempString;
                    }
                }
            }
            else if(temp==0)
            {
                tempString="00000000";
            }
            else
            {
                tempString=Integer.toBinaryString(-temp-1);
                if(tempString.length()<8);
                {
                    for(int i=0;i<8-tempString.length();i++)
                    {
                        tempString="0"+tempString;
                    }
                }
                for(int i=0;i<tempString.length();i++)
                {
                    convertString=convertString+convert01(tempString.charAt(i));
                }
                tempString=convertString;
            }
            for(int i=0;i<tempString.length();i++)
            {
                characterArrayList.add(tempString.charAt(i));
            }
        }
        return characterArrayList;
    }

    public Map makeCodeTable(ArrayList<Byte> codebox)
    {
        byte[] b=new byte[codebox.size()];
        for(int i=0;i<codebox.size();i++)
        {
            b[i]=codebox.get(i);
        }
        char[] c=getChars(b);
        Map<String,Character> huffcode=new HashMap<>();
        char tempchar=c[0];
        String tempcode="";
        for(int i=1;i<c.length;i++)
        {
            if(c[i]!='0'&&c[i]!='1'&&(c[i-1]=='0'||c[i-1]=='1'))
            {
                huffcode.put(tempcode,tempchar);
                tempchar=c[i];
                tempcode="";
            }
            else
            {
              tempcode=tempcode+c[i];
            }
        }
        return huffcode;
    }

    public void qushu(String buffer, ArrayList<Character> characterArrayList)
    {
        buffer=buffer+characterArrayList.get(0);
        characterArrayList.remove(0);
    }

    public ArrayList translation(ArrayList<Character> characterArrayList,Map<String,Character> huffcode)
    {
        ArrayList<Character> primitiveText=new ArrayList<>();
        String buffer="";
        while(characterArrayList.size()>0)
        {
            qushu(buffer,characterArrayList);
            if(huffcode.containsKey(buffer))
            {
                primitiveText.add(huffcode.get(buffer));
                buffer="";
            }

        }
        return primitiveText;
    }

    public void writefile(String destpath,ArrayList<Character> primitiveText)
    {
        char[] c=new char[primitiveText.size()];
        for(int i=0;i<primitiveText.size();i++)
        {
            c[i]=primitiveText.get(i);
        }
        byte[] bytes=getBytes(c);
        try {
            FileOutputStream fos=new FileOutputStream(destpath);
            fos.write(bytes);
            fos.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
