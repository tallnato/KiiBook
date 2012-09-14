
package kii.kiibook.Student.database;

import objects.ClassPeople;
import objects.MediaBook;
import objects.MyCalendar;
import objects.NewEvent;
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
    private Set<String>                  blockedApps       = new HashSet<String>();
    private final ArrayList<Summary>     listSummaries     = new ArrayList<Summary>();
    private ArrayList<NewEvent>          listEvents        = new ArrayList<NewEvent>();
    private final ObjectCreator          creator;
    private final ArrayList<ClassPeople> classes;
    
    private DataShared() {
    
        creator = new ObjectCreator();
        myProfile = creator.createStudent(false);
        classes = creator.getClasses();
        myCalendar = creator.getMyCalendar();
        
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 9, 12);
        
        ArrayList<MediaBook> medias = new ArrayList<MediaBook>();
        
        listSummaries.add(new Summary(medias, "Aula de Dúvidas para preparaçãoo do Teste.", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 14);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 1, "Page 1"));
        listSummaries.add(new Summary(medias, "Entrega de trabalhos de Grupo!\nTeste de Avaliaçãoo!", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 19);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 3, "Page 3"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Revisãoo de números racionais não negativos.!\nIntroduçãoo da multiplicçãoo de números racionais não negativos.\nResoluçãoo de Exercícios:",
                                        cal.getTime(), listSummaries.size()));
        cal.set(2012, 9, 21);
        medias.add(new MediaBook("book5", 4, "Page 4"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Correçãoo do trabalho de casa do dia 3/9/2012.\nContinuação do tópico multiplicçãoo de números racionais não negativos.\nResolução de Exercícios:",
                                        cal.getTime(), listSummaries.size()));
        
    }
    
    public ArrayList<ClassPeople> getClasses() {
    
        return classes;
    }
    
    public ArrayList<Summary> getListSummaries() {
    
        return listSummaries;
    }
    
    public ArrayList<NewEvent> getListEvents() {
    
        return listEvents;
    }
    
    public void setListEvents( ArrayList<NewEvent> listEvents ) {
    
        this.listEvents = listEvents;
    }
    
    public static DataShared getInstance() {
    
        if (instance == null) {
            instance = new DataShared();
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
    
        return blockedApps;
    }
    
    public void setBlockedApps( Set<String> blockedApps ) {
    
        this.blockedApps = blockedApps;
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
