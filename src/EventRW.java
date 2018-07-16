import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventRW {
    public static File logoutfile = new File(LocalDate.now().toString() + "GZTagerLog.markdown");
    public static OutputStream logstream;
    public static void Write(Exception e) {
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write(("> " + LocalDateTime.now().toString() + " Exception : " + e.toString() + "\r\n").getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.println(e.toString());
        e.printStackTrace();
    }

    public static void Write(String event){
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write((LocalDateTime.now().toString() + " Exception : " + event + "\r\n").getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.println(event);
    }
}
