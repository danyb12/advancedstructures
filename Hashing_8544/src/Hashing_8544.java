/* Dhanush Sureshbabu cs435 8544 miniproject Hashing*/
import java.io.*;
import java.io.*;
import java.util.*;


public class Hashing_8544 {

    private int m;
    private int siz;
    private int head;
    private int[] T;
    public String A;
    public static String null_char="\0";
    public Lexicon HashCreate(Lexicon L, int mm) {

        L = new Lexicon(mm);

        m=L.getM();
        siz=L.getSiz();
        head=L.getHead();
        T=L.getT();
        A=L.getA();
        return L;
    }

    public int hash(String word){
        char[] letters = word.toCharArray();
        int sum = 0;
        for(int i = 0; i < letters.length; i++){
            sum += (int) letters[i];
        }
        return (sum % m);
    }

    public int rehash(int hash, int iter){
        //return ((hash + Math.pow(iter, 2)) % m);
        return ((hash + (iter*iter)) % m);
    }
    public Boolean HashEmpty(Lexicon L) {

        return L.isEmpty();

    }

    public Boolean HashFull(Lexicon L) {


        return L.isFull();

    }

    public void HashPrint(Lexicon L) {

        System.out.println("   T\t\tA: " + A.replace("\0","\\"));
        for(int i = 0; i < T.length; i++){
            if(T[i] != -1) System.out.println(i + ": " + T[i]);
            else System.out.println(i + ": ");
        }

    }

    public int HashInsert(Lexicon L, String word) {



        if(L.isFull()){
            m = 2*m;
            T = Arrays.copyOf(T, m);
            Arrays.fill(T, (m/2)+1, m, -1);
        }
        int t = hash(word);
        int i = 1;
        while(T[t] != -1){
            if(i >= m) return -1;
            t = rehash(t, i++);
        }
        if(L.isEmpty()){
            A = word + null_char + new String(new char[15*m - word.length()-1]).replace("\0", " ");
            head += 1;
            T[t] = head;
            head += word.length();
            return t;
        }
        A = A.substring(0, head+1) + word + null_char + A.substring(head+word.length()+1);
        T[t] = ++head;
        head += word.length();

        return t;





    }

    public int HashSearch(Lexicon L, String word) {

        int t = hash(word);
        int i = 1;
        String result;
        if(T[t] == -1){
            result = "\r\n";
        }
        else{
            result = A.substring(T[t], A.indexOf("\0", T[t]));
        }
        while(!(result.equals(word))){
            if(i >= m){
                return -1;
            }
            t = rehash(t, i++);
            if(T[t] == -1){
                t = rehash(t, i++);
                continue;
            }
            else result = A.substring(T[t], A.indexOf("\0", T[t]));
        }
        return t;


    }

    public  int HashDelete(Lexicon L, String word) {
        int t = HashSearch(L,word);
        if(t == -1) return -1;
        A = A.substring(0, T[t]) + new String(new char[word.length()]).replace("\0", "*")
                + null_char + A.substring(T[t] + word.length() + 1);
        T[t] = -1;
        return t;
    }


    public void HashBatch(Lexicon L, String file) throws IOException{
        try{
            BufferedReader reader;
            FileReader filer;
            filer=(new FileReader(file));
            reader = new BufferedReader(filer);
            String add="";
            String line = reader.readLine();
            int opt;
            String word = "";
            Boolean created = false;
            while(line != null){
                opt = Integer.parseInt(line.substring(0,2));
                if(opt != 13){
                    word = line.substring(3);
                    if((word.length())==3){
                         add=" ";
                    }
                    else{
                        add="";
                    }
                }
                if((!created) && (opt != 14)){
                    System.out.println("failed to execute: " + opt + " " + word + "\t; must create a lexicon first (operation 14)");
                    line = reader.readLine();
                    continue;
                }
                switch(opt){


                    case 15:
                        break;
                    case 14://Operation 14 is Create
                        L = HashCreate(L, Integer.parseInt(word));
                        created = true;
                        break;

                    case 13://Operation 13 is Print
                        HashPrint(L);
                        break;
                    case 12://Operation 12 is Search
                        opt = HashSearch(L, word);
                        if(opt == -1){
                            System.out.println(word + add+"\tnot found");
                            break;
                        }
                        System.out.println(word + add+ "\tfound at slot " + opt);
                        break;

                    case 11://Operation 11 is Deletion
                        opt = HashDelete(L, word);
                        if(opt == -1){
                            System.out.println(word+ add +"\tfailed to find/delete");
                            break;
                        }
                        System.out.println(word + add +"\tdeleted from slot " + opt);
                        break;

                    case 10://Operation 10 is Insert
                        opt = HashInsert(L, word);
                        if(opt == -1){
                            System.out.println(word + add + "\tfailed to insert");
                            break;
                        }
                        break;
                    default:
                        System.out.println("Unhandled: " + opt + " " + word);
                        break;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            Lexicon lexicon = null;
            Hashing_8544 hash=new Hashing_8544();

            hash.HashBatch(lexicon, args[0]);
            return;
        }
    }

}
class Lexicon {
    private int m;
    private int siz;
    private int head;
    private int[] T;
    public String A;

    public Lexicon(int M){
        m = M;
        head = -1;
        T = new int[m];
        Arrays.fill(T, -1);
        siz = 15*m;
        A = new String(new char[siz]).replace("\0", " ");
    }
    public int getM(){
        return m;
    }
    public int getSiz(){
        return siz;
    }
    public int getHead(){
        return head;
    }
    public int[] getT(){
        return T;
    }
    public String getA(){
        return A;
    }



    public Boolean isEmpty(){
        for(int i = 0; i < m; i++) if(T[i] != -1) return false;
        return true;
    }

    public Boolean isFull(){
        for(int i = 0; i < m; i++) if(T[i] == -1) return false;
        return true;
    }
}













