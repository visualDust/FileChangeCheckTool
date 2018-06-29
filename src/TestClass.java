import java.io.File;

public class TestClass {
    public static void main(String[] args){
        File myFile_ = new File("/media/visualdust/GZT_DOCS/","");
        File Fl[] = myFile_.listFiles();
        for(int i =0;i<Fl.length;i++){
            System.out.println(Fl[i].getName()+" : "+Fl[i].hashCode());
        }
    }
}
