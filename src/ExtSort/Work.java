/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ExtSort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import javax.swing.JTextArea;

/**
 *
 * @author andrey
 */
public class Work {
    private cFile[] source;
    private cFile[] dest;
    
    public Work(){
        source = new cFile[2];
        dest = new cFile[2];        
    }
    
    public void writesec(cFile dest) throws IOException{
        source[0].nextsec();
        source[1].nextsec();
        while (!source[0].isEosec() && !source[1].isEosec()){
            if (source[0].getLast()<source[1].getLast())
                source[0].copy(dest);
            else
                source[1].copy(dest);
        }
        source[0].copysec(dest);
        source[1].copysec(dest);
    }
    
    public void Sort(String s, Info inf) throws FileNotFoundException, IOException{
        source[0] = new cFile(s);
        source[1] = new cFile("temp2.tmp");
        dest[0] = new cFile("temp3.tmp");
        dest[1] = new cFile("temp4.tmp");
        int sumsec = 0;
        cFile buf;
        int idest = 0;
        int cnt=0;
        int see=0;
        do{
            
            source[0].openread();
            source[1].openread();
            dest[0].openwrite();
            dest[1].openwrite();
            
            sumsec = 0;
            idest = 0;
            if (!source[0].isEof() || !source[1].isEof()){
                writesec(dest[0]);
                sumsec++;
                see++;
            }
            if (!source[0].isEof() || !source[1].isEof()){
                writesec(dest[1]);
                sumsec++;
                see++;
            }            
            while (!source[0].isEof() || !source[1].isEof()){
                writesec(dest[idest]);
                sumsec++;
                see++;
                idest = 1 - idest;
            }
            source[0].closefile();
            source[1].closefile();
            dest[0].closefile();
            dest[1].closefile();
            
            buf = source[0];
            source[0] = dest[0];
            dest[0] = buf;
            
            buf = source[1];
            source[1] = dest[1];
            dest[1] = buf;
            
            cnt++;
            
        }while(sumsec>1);            
        source[0].delete();
        source[1].delete();
        dest[0].delete();
        dest[1].delete();
        inf.setInfo(cnt,see);
    }
    
    public RandomAccessFile randomfile(String s, int len) throws FileNotFoundException, IOException{
        Random rand = new Random();
        RandomAccessFile f = new RandomAccessFile(s,"rw");
        for(int i = 0;i < len; i++)
            f.writeInt(rand.nextInt(1000));
        f.close();
        return f;
    }
    
    public RandomAccessFile reversefile(String s, int len) throws FileNotFoundException, IOException{
        RandomAccessFile f = new RandomAccessFile(s,"rw");
        for (int i=len-1;i>=0;i--)
            f.writeInt(i);
        f.close();
        return f;
    }
    
}
