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
    private cFile ff1;
    private cFile ff2;
    private cFile ff3;
    private cFile ff4;
    
    public void writesec(cFile f1, cFile f2, cFile f0) throws IOException{
        f1.nextsec();
        f2.nextsec();
        while (!f1.isEosec() && !f2.isEosec()){
            if (f1.getLast()<f2.getLast())
                f1.copy(f0);
            else
                f2.copy(f0);
        }
        f1.copysec(f0);
        f2.copysec(f0);        
    }
    
    public void Distribute(cFile f0, cFile f1, cFile f2,Info inf) throws IOException{
        int i = 1;
        int w=0;
        while (!f0.isEof()){
            f0.nextsec();
            i = 1-i;
            if (i==0)
                f0.copysec(f1);
            else
                f0.copysec(f2);
            w++;
        }
        inf.setWrite(w);
    }
    
    public void Copy(cFile f1, cFile f2) throws IOException{
        f1.open();
        f2.openwrite();
        while (!f1.isEof()){
            f2.getF().writeInt(f1.getF().readInt());
        }
        f1.closefile();
        f2.closefile();
    }
    
    public int Merge(cFile f1, cFile f2, cFile f3, cFile f4,Info inf) throws IOException{
        int w = inf.write;
        int result = 0;
        f1.openread();
        f2.openread();
        f3.openwrite();
        f4.openwrite();
        while (!f1.isEof() || !f2.isEof()){
            writesec(f1,f2,f3);
            w++;
            result++;
            if (!f1.isEof() || !f2.isEof()){
                writesec(f1,f2,f4);
                w++;
                result++;
            }
        }
        /*f1.getFin().close();
        f2.getFin().close();
        f3.getFout().close();
        f4.getFout().close();*/
        f1.closefile();
        f2.closefile();
        f3.closefile();
        f4.closefile();
        inf.setWrite(w);
        return result;
    }
    
    public void Sort(String s, Info inf) throws FileNotFoundException, IOException{
        ff1 = new cFile(s);
        ff2 = new cFile("temp2.tmp");
        ff3 = new cFile("temp3.tmp");
        ff4 = new cFile("temp4.tmp");
        int sumsec = 0;
        int cnt=0;
        boolean OK = false;
        ff1.openread();
        if (ff1.isempty()){
            ff1.closefile();
            return;
        }
        ff3.openwrite();
        ff4.openwrite();
        Distribute(ff1,ff3,ff4,inf);
        /*ff1.getFin().close();
        ff3.getFout().close();
        ff4.getFout().close();*/
        ff1.closefile();
        ff3.closefile();
        ff4.closefile();       
        do{
            OK = !OK;
            sumsec = Merge(ff3,ff4,ff1,ff2,inf);
            if (sumsec>1){
                OK = !OK;
                sumsec += Merge(ff1,ff2,ff3,ff4,inf);
            }
            cnt++;
        }while(sumsec>1);
        if (!OK)
            Copy(ff3,ff1);
        ff2.delete();
        ff3.delete();
        ff4.delete();
        inf.setCount(cnt);
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
