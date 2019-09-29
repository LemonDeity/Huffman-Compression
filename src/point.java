public class point implements Comparable<point>{
    char character;
    Integer frequency;
    //This is for creating first instance of the data point
    public point(char c){
        character = c;
        frequency = 1;
    }

    public point(char c, int f){
        character = c;
        frequency = f;
    }
    //This increases the frequency by 1;
    public void update(){
        frequency++;
    }

    public int compareTo(point p){
        //return -1 if less than p 0 if the same and 1 if greater than p
        return frequency.compareTo(p.frequency);
    }

    public String toString(){
        String str = "\'";
        if (character == '\n'){
            str += "\\n";
        }
        else{
            str += character;
        }
        str += "\'"+frequency+" ";
        return str;
    }
}
