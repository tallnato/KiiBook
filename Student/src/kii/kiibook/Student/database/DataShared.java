
package kii.kiibook.Student.database;

import objects.ClassPeople;
import objects.MediaBook;
import objects.MyCalendar;
import objects.ObjectCreator;
import objects.Student;
import objects.Summary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class DataShared {
    
    private static DataShared            instance;
    private final Student                myProfile;
    private ArrayList<Student>           listFriendsOnline = new ArrayList<Student>();
    private ArrayList<MyCalendar>        myCalendar;
    private final Set<String>            blockedApps       = new HashSet<String>();
    private final ArrayList<Summary>     listSummaries     = new ArrayList<Summary>();
    private final ObjectCreator          creator;
    private final ArrayList<ClassPeople> classes;
    
    private DataShared() {
    
        creator = new ObjectCreator();
        myProfile = creator.createStudent(true);
        classes = creator.getClasses();
        myCalendar = creator.getMyCalendar();
        
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 9, 12);
        
        ArrayList<MediaBook> medias = new ArrayList<MediaBook>();
        
        listSummaries.add(new Summary(medias, "Aula de D��vidas para prepara����o do Teste.", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 14);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 1, "Page 1"));
        listSummaries.add(new Summary(medias, "Entrega de trabalhos de Grupo!\nTeste de Avalia����o!", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 19);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 3, "Page 3"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Revis��o de n��meros racionais n��o negativos.!\nIntrodu����o �� multiplica����o de n��meros racionais n��o negativos.\nResolu����o de Exerc��cios:",
                                        cal.getTime(), listSummaries.size()));
        cal.set(2012, 9, 21);
        medias.add(new MediaBook("book5", 4, "Page 4"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Corre����o do trabalho de casa do dia 3/9/2012.\nContinua����o do t��pico multiplica����o de n��meros racionais n��o negativos.\nResolu����o de Exerc��cios:",
                                        cal.getTime(), listSummaries.size()));
        
    }
    
    public ArrayList<ClassPeople> getClasses() {
    
        return classes;
    }
    
    public ArrayList<Summary> getListSummaries() {
    
        return listSummaries;
    }
    
    public static DataShared getInstance() {
    
        if (instance == null) {
            instance = new DataShared();
            System.out.println("cenas construtor");
        }
        return instance;
    }
    
    public Student getMyProfile() {
    
        return myProfile;
    }
    
    public ArrayList<MyCalendar> getMyCalendar() {
    
        return myCalendar;
    }
    
    public Set<String> getBlockedApps() {
    
        System.out.println("count " + blockedApps);
        return blockedApps;
    }
    
    public void setBlockedApps( Set<String> blockedApps ) {
    
        this.blockedApps.clear();
        this.blockedApps.addAll(blockedApps);
        System.out.println("count " + this.blockedApps);
    }
    
    public void setMyCalendar( ArrayList<MyCalendar> myCalendar ) {
    
        this.myCalendar = myCalendar;
    }
    
    public ArrayList<Student> getListFriendsOnline() {
    
        return listFriendsOnline;
    }
    
    public void setListFriendsOnline( ArrayList<Student> listFriendsOnline ) {
    
        this.listFriendsOnline = listFriendsOnline;
    }
    
}
