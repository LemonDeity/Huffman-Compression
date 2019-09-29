import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException{
        String[] arr = new String[] {"War and Peace.txt", "Little Women.txt",
                "A Tale of Two Cities.txt", "Communist Manifesto.txt",
                "dream.txt","The Three Musketeers.txt", "Moby Dick.txt",
                "Much Ado About Nothing.txt","Magna Carta.txt","Tom Sawyer.txt",
                "Anna Karenina.txt","Anna Karenina - Diff.txt","CompressionTest.txt",
                "Amendments to the Constitution.txt","Treasure Island.txt","SmallCompressionTest.txt"};

        //War and Peace >>> 0
        //Little Women >>> 1
        //A Tale of Two Cities >>> 2
        //Communist Manifesto >>> 3
        //dream >>> 4
        //The Three Musketeers >>> 5
        //Moby Dick >>> 6
        //Much Ado About Nothing >>> 7
        //Magna Carta >>> 8
        //Tom Sawyer >>> 9
        //Anna Karenina >>> 10
        //Anna Karenina - Diff>>> 11
        //CompressionTest >>> 12
        //Amendments to the Constitution >>> 13
        //Treasure Island >>> 14
        //SmallCompressionTest >>> 15

        ArrayList<PriorityQueue<point>> list = inputToPriority(arr);

        PriorityQueue<Node> nodeQ = new PriorityQueue<>();
        //for easy use in testing
        int ind = 5;
        while(!list.get(ind).isEmpty()){
            nodeQ.add(new Node(list.get(ind).poll()));
        }

        while(nodeQ.size() > 1){
            //This removes the smallest Nodes
            Node one = nodeQ.poll();
            Node two = nodeQ.poll();
            Node three = new Node(one,two);
            nodeQ.add(three);
        }

        //System.out.println(nodeQ.peek());
        nodeQ.peek().pPrint();
        HashMap<Character, String> k = nodeQ.peek().compressMap();
        System.out.println();

        Scanner test = new Scanner(new File(arr[ind]));
        for (int i = 0; test.hasNext(); i++) {
            if (i == 2784 || i == 3256 || i == 102913){
                System.out.println(test.next());
            }
            else{
                test.next();
            }
        }

        /*System.out.println(k.get('A'));
        System.out.println(k.get('b'));
        System.out.println(k.get('C'));
        System.out.println(k.get('d'));
        System.out.println(k.get('\n'));
        System.out.println(k.get('e'));
        char etx = 3;
        System.out.println(etx);*/
        int[] tran = translate(k,arr[ind]);
        byte[] b = convertIntArray(tran);
        BitOutputStream stream = new BitOutputStream("Third Compressed file.txt");
        for(int i = 0; i < b.length; i++){
            stream.write(b[i]);
        }
        stream.close();

        unCompress(k,"Third Compressed file.txt","Third UnCompressed file.txt");
    }
    public static HashMap<String,Character> keyValueSwitch(HashMap<Character,String> map){
        HashMap map1 = new HashMap();
        Set s = map.keySet();
        Iterator it = s.iterator();
        while(it.hasNext()){
            Object key = it.next();
            map1.put(map.get(key),key);
        }
        return map1;
    }

    public static void unCompress(HashMap<Character,String>map1, String fileName,String newUncompressedFIleName)throws IOException{
        HashMap<String, Character> map = keyValueSwitch(map1);
        BitInputStream in = new BitInputStream(fileName);
        BitOutputStream out = new BitOutputStream(newUncompressedFIleName);
        int g = 0;
        String code = "";
        while(true){
            g = in.readBits(1);
            if (g == -1){
                break;
            }
            if (g == 0){
                code += "0";
            }//will refactor later to just be an else statement
            else if(g == 1){
                code += "1";
            }

            if (map.get(code) != null){
                out.write(map.get(code));
                code = "";
            }
        }
        out.close();
    }

    public static ArrayList<PriorityQueue<point>> inputToPriority(String[] arr)throws IOException{
        ArrayList<PriorityQueue<point>> list = new ArrayList<>();
        for (String str:arr) {
            list.add(new PriorityQueue<>(convert(new Scanner(new File(str))).values()));
        }
        return list;
    }

    public static HashMap<Character,point> convert(Scanner input){
        ArrayList<String> list = new ArrayList<>();
        while(input.hasNext()){
            list.add(input.nextLine());
        }
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.remove(0);
        }
        return analyze(arr);
    }

    public static HashMap<Character, point> analyze(String[] arr){
        //This will go through every line and input the
        HashMap<Character,point> map = new HashMap<>();
        for (int i = 0; i < arr.length ; i++) {
            char[] chars = arr[i].toCharArray();
            for (char c : chars) {
                if (!map.containsKey(c)){
                    //This happens if this char hasn't been encountered yet and hasn't been inputted in yet
                    map.put(c,new point(c));
                }
                else{
                    //This code block runs when the char has been encountered
                    map.get(c).update();
                }
            }
            if (!map.containsKey('\n')){
                map.put('\n',new point('\n'));
            }
            else{
                map.get('\n').update();
            }
        }
        return map;
    }

    //Will refactor later meaning I wil remove this later to make sure conversion isn't needed
    public static byte[] convertIntArray(int[] arr){
        byte[] b = new byte[arr.length];
        for(int i = 0; i < b.length; i++){
            b[i] = (byte)arr[i];
        }
        return b;
    }

    public static void createCompressedFile(HashMap<Character,String> map, String fileName)throws IOException{
        int[] arr = translate(map,fileName);
        for(int i : arr){
            System.out.println(i);
        }
    }
    //actually creates the compressed thing
    //Will refactor to instead return byte[]
    public static int[] translate(HashMap<Character,String> map, String fileName)throws IOException{
        ArrayList<Integer> list = new ArrayList<>();
        Scanner in = new Scanner(new File(fileName));
        while(in.hasNextLine()){
            char[] line = in.nextLine().toCharArray();
            for(char c : line){
                try{
                    char[] code = map.get(c).toCharArray();
                    for (char co : code) {
                        list.add(co-48);
                    }
                }catch(Exception NullPointerException){
                    System.out.println(list.size());
                }
            }
            //This is to make sure the nextLines are inside the compressed file
            char[] code = map.get('\n').toCharArray();
            for(char co : code){
                list.add(co-48);
            }
        }

        ArrayList<Integer> lis2 = new ArrayList<>();

        for (int i = 0; i < (list.size()/8)+1; i++) {
            //This is for the binary conversion
            int power = 7;
            int end = 0;
            for (int j = i*8; j < ((i*8)+8) && j < list.size(); j++) {
                list.get(j);
                if (list.get(j) == 1){
                    //This means end needs to be updated
                    end += Math.pow(2,power);
                }
                power--;
            }
            lis2.add(end);
        }

        int[] arr = new int[lis2.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = lis2.get(i);
        }

        return arr;
    }
}

