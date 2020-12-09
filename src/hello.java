import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class hello
{
    public static void main(String args[])
    {
        String content=null;
        ArrayList<Integer> list=new ArrayList<>();
        try {
            FileInputStream fis=new FileInputStream("C:\\Users\\28689\\IdeaProjects\\心情\\src\\input.txt");
            FileOutputStream fos=new FileOutputStream("C:\\Users\\28689\\IdeaProjects\\心情\\src\\if.mwh");
            byte[] buffer=new byte[100];
            byte[] buffer2=new byte[1];
            int size=0;
            while ((size=fis.read(buffer))!=-1)
            {
                content=new String(buffer,0,size);
                for(int i=0;i<content.length();i++)
                {
                    if(content.charAt(i)=='0')
                    {
                        list.add(0);
                    }
                    else
                        list.add(1);

                }
                //fos.write(buffer);
            }
            fis.close();
            fos.close();
            System.out.println(list.get(1));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}