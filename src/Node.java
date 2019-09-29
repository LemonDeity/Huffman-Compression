import java.util.HashMap;

public class Node implements Comparable<Node>{
    point value;
    Node left,right;
    Integer size;
    public Node(point p){
        value = p;
        size = value.frequency;
    }

    public Node(Node r, Node l){
        value = new point('*',r.getValue().frequency+l.getValue().frequency);
        left = l;
        right = r;
        size = value.frequency;
    }

    public void setLeft(Node n){
        left = n;
    }

    public void setRight(Node n){
        right = n;
    }

    public int compareTo(Node n){
        //do not change this is correct
        return size.compareTo(n.size);
    }


    public point getValue(){
        return value;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRIght(){
        return right;
    }

    public void pPrint(){
        pPrint(0);
    }
    //Go right first
    public void pPrint(int depth){

        if (right == null) {
        }else{
            right.pPrint(depth+1);
        }
        space(depth);
        if(left == null){
        }else{
            left.pPrint(depth+1);
        }

    }

    public void space(int spaces){
        for (int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }
        System.out.println(value);
    }

    public HashMap<Character,String> compressMap(){
        HashMap<Character,String> map = new HashMap<>();
        String code = "";
        return compressMap(map,code);
    }

    private HashMap<Character,String> compressMap(HashMap<Character,String> map, String code){
        //This means the node is not a leaf node and has two children
        if(value.character == '*'){
            if (left != null){
                map = left.compressMap(map,code+"0");
            }

            if(right != null){
                map = right.compressMap(map,code+"1");
            }
        }
        else{
            map.put(value.character,code);
        }
        return map;
    }

    public String toString(){
        String str = value.toString();
        if (left != null){
            str += left.toString();
        }

        if(right != null){
            str += right.toString();
        }
        return str;
    }

}
