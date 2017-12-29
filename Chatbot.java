
import java.util.*;
import java.io.*;

class Triplet{
    private final int i;
    private final double l;
    private final double r;

    Triplet(int i, double l, double r){
        this.i = i;
        this.l = l;
        this.r = r;
    }
    int getInd() {return i; };
    double getLi(){ return l; }
    double getRi(){ return r; }
}


public class Chatbot{
    private static String filename = "./WARC201709_wid.txt";
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }

    private static int probWord(int w, ArrayList<Integer> corpus){
        return Collections.frequency(corpus, w);
    }

    private static Triplet getRange(double rno, ArrayList<Integer> corpus){

        Map<Integer,Integer> map = new TreeMap<>();
        List<Triplet> uniProbDis = new ArrayList<>();
        Iterator<Integer> iterator = corpus.iterator();
        Integer ind = 0;
        double Li = 0.0 , Ri = 0.0;

        while(iterator.hasNext()){
            Integer temp = iterator.next();
            if(map.containsKey(temp)){
                map.put(temp, map.get(temp)+1);
            }
            else{
                map.put(temp, 1);
            }
        }
        Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
        double currprob = 0.0;
        while(entries.hasNext()){
            Map.Entry<Integer, Integer> entry = entries.next();
            double prob = entry.getValue()/(double)corpus.size();
            if(prob ==0)
                continue;
            Li = (ind !=0)? currprob : 0.0;
            currprob += prob;
            Ri = (ind !=0)? currprob : prob;

            Triplet a = new Triplet(entry.getKey(), Li , Ri);
            uniProbDis.add(a);
            ind++;
        }

        //rno = (double)n1/n2;

        Iterator<Triplet> it = uniProbDis.iterator();

        while(it.hasNext()) {
            Triplet b = it.next();
            if (rno == 0) {
                if (rno >= b.getLi() && rno <= b.getRi()) {
                    return b;
                }
            }
            else if (rno > b.getLi() && rno <= b.getRi()) {
                    return b;
                }
        }

        return null;
    }

    private static ArrayList<Integer> findAfterH(int h, ArrayList<Integer> corpus){
        //Iterator<Integer> iterator = corpus.iterator();
        ArrayList<Integer> word_after_h = new ArrayList<>();

        for(int i= 0 ; i< corpus.size() ; i++ ){
            if((corpus.get(i) == h) && (i != corpus.size())) {
                word_after_h.add(corpus.get(i+1));
            }
        }

        return word_after_h;

    }

    private static ArrayList<Integer> findAfterH1H2(int h1, int h2, ArrayList<Integer> corpus){

        ArrayList<Integer> word_after_h1h2 = new ArrayList<>();

        for(int i= 0 ; i< corpus.size() ; i++ ){
            if((corpus.get(i) == h1) && (corpus.get(i+1) == h2) && (i != corpus.size()) && ((i+1) != corpus.size())) {
                word_after_h1h2.add(corpus.get(i+2));
            }
        }

        return word_after_h1h2;

    }

    static public void main(String[] args){
        ArrayList<Integer> corpus = readCorpus();
        int flag = Integer.valueOf(args[0]);

        if(flag == 100){
            int w = Integer.valueOf(args[1]);
            int count = 0;
            //TODO count occurence of w
            count = probWord(w, corpus);
            System.out.println(count);
            System.out.println(String.format("%.7f",count/(double)corpus.size()));
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            //TODO generate
            double rno = (double)n1/n2;
            Triplet val = getRange(rno, corpus);
            if(val != null)
                System.out.println(String.format("%d\n%.7f\n%.7f", val.getInd(), val.getLi(), val.getRi()));

        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = findAfterH(h,corpus); //new ArrayList<Integer>();
            //TODO
            count = probWord(w, words_after_h);
            //output
            System.out.println(count);
            System.out.println(words_after_h.size());
            System.out.println(String.format("%.7f",count/(double)words_after_h.size()));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            //TODO
            ArrayList<Integer> words_after_h = findAfterH(h,corpus);

            double rno = (double)n1/n2;
            Triplet val = getRange(rno, words_after_h);
            if(val != null)
                System.out.println(String.format("%d\n%.7f\n%.7f", val.getInd(), val.getLi(), val.getRi()));

        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;
            ArrayList<Integer> words_after_h1h2 = findAfterH1H2(h1, h2, corpus);
            count = probWord(w, words_after_h1h2);
            System.out.println(count);
            System.out.println(words_after_h1h2.size());
            if(words_after_h1h2.size() == 0)
                System.out.println("undefined");
            else
                System.out.println(String.format("%.7f",count/(double)words_after_h1h2.size()));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            //TODO

            ArrayList<Integer> words_after_h1h2 = findAfterH1H2(h1,h2,corpus);
            double rno = (double)n1/n2;
            Triplet val = getRange(rno, words_after_h1h2);
            if(val != null)
                System.out.println(String.format("%d\n%.7f\n%.7f", val.getInd(), val.getLi(), val.getRi()));
            else
                System.out.println("undefined");

        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rng.nextDouble();
                Triplet val = getRange(r, corpus);
                h1 = val.getInd();

                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }
                // TODO Generate second word using r
                r = rng.nextDouble();
                ArrayList<Integer> words_after_h = findAfterH(h1, corpus);
                Triplet val2 = getRange(r, words_after_h);
                h2 = val2.getInd();
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rng.nextDouble();
                ArrayList<Integer> words_after_h = findAfterH(h1, corpus);
                Triplet val = getRange(r, words_after_h);
                h2 = val.getInd();
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w =0 ;
                // TODO Generate new word using h1,h2
                ArrayList<Integer> words_after_h1h2 = findAfterH1H2(h1,h2, corpus);
                Triplet val3 = getRange(r, words_after_h1h2);
                if(val3 != null)
                w = val3.getInd();
                else {
                    ArrayList<Integer> words_after_h = findAfterH(h2, corpus);
                    Triplet val2 = getRange(r, words_after_h);
                    if(val2 != null)
                    w = val2.getInd();
                    else{
                        Triplet val = getRange(r, corpus);
                        w = val.getInd();
                    }
                }
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
