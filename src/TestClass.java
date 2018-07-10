import java.io.File;

public class TestClass {
    public static void main(String[] args){
        File myFile_ = new File("C:\\Users\\VisualDust\\Desktop\\FileChangeCheckTool.jar");
        System.out.println(myFile_.getTotalSpace()/1024/1024/1024);
//        File Fl[] = myFile_.listFiles();
//        for(int i =0;i<Fl.length;i++){
//            System.out.println(Fl[i].getName()+" : "+Fl[i].hashCode());
//        }
    }
}
