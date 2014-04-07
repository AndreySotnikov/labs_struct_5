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

/**
 *
 * @author andrey
 */
public class cFile {
    private RandomAccessFile f;

    public RandomAccessFile getF() {
        return f;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public boolean isEosec() {
        return eosec;
    }

    public void setEosec(boolean eosec) {
        this.eosec = eosec;
    }

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }
    private String fname;
    private int last;
    private boolean eosec;
    private boolean eof;
    
    public cFile(String s) throws FileNotFoundException{
        fname = s;
        f = new RandomAccessFile(fname,"rw");
    }
    
    public void copy(cFile f2) throws IOException{
        f2.f.writeInt(last);
        f2.last = last;
        eof = f.getFilePointer()==f.length();
        if (!eof)
            last = f.readInt();
        eosec = eof || (f2.last>last);
    }
    
    public void copysec(cFile f2) throws IOException{
        while (!eosec)
            copy(f2);
    }
    
    public void delete() throws IOException{
        File file = new File(fname);
        file.delete();
    }
    
    public boolean isempty() throws IOException{
        return f.length()==0;
    }
    
    public void nextsec(){
        eosec = eof;
    }
    
    public void openread() throws IOException{
        f = new RandomAccessFile(fname,"rw");
        eof = f.getFilePointer()==f.length();
        eosec = eof;
        if (!eof)
            last = f.readInt();
    }
    
    public void openwrite() throws FileNotFoundException, IOException{
        this.delete();
        f = new RandomAccessFile(fname,"rw");
    }
    
    public void closefile() throws IOException{
        f.close();
    }
    
}
