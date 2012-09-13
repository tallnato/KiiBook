
package kii.kiibook.managerclass.database;

import objects.ClassPeople;
import objects.MediaBook;
import objects.MyCalendar;
import objects.ObjectCreator;
import objects.Profile;
import objects.Student;
import objects.Summary;
import util.SlaveStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import kii.kiibook.managerclass.adapters.ItemSummaryListView;
import kii.kiibook.teacher.R;

public class DataShared {
    
    private static DataShared            instance;
    
    private final Profile                myProfile;
    private final ArrayList<ClassPeople> classes;
    private ArrayList<MyCalendar>        myCalendar;
    private ArrayList<Student>           students;
    
    private final ObjectCreator          creator;
    
    private ArrayList<Student>           slavesOn;
    
    private ArrayList<Student>           slavesAll;
    private final ArrayList<Summary>     listSummaries = new ArrayList<Summary>();
    ArrayList<ItemSummaryListView>       events        = new ArrayList<ItemSummaryListView>();
    
    private DataShared() {
    
        creator = new ObjectCreator();
        myProfile = creator.createTeacher();
        myCalendar = creator.getMyCalendar();
        classes = creator.getClasses();
        
        students = creator.getTurma().getStudents();
        slavesOn = new ArrayList<Student>();
        slavesAll = new ArrayList<Student>();
        slavesAll.clear();
        slavesAll.addAll(slavesOn);
        slavesAll.addAll(students);
        
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 9, 12);
        
        ArrayList<MediaBook> medias = new ArrayList<MediaBook>();
        
        listSummaries.add(new Summary(medias, "Aula de Dúvidas para preparação do Teste.", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 14);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 1, "Page 1"));
        listSummaries.add(new Summary(medias, "Entrega de trabalhos de Grupo!\nTeste de Avaliação!", cal.getTime(), listSummaries.size()));
        
        cal.set(2012, 9, 19);
        medias = new ArrayList<MediaBook>();
        medias.add(new MediaBook("book5", 3, "Page 3"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Revisão de números racionais não negativos.!\nIntrodução à multiplicação de números racionais não negativos.\nResolução de Exercícios:",
                                        cal.getTime(), listSummaries.size()));
        cal.set(2012, 9, 21);
        medias.add(new MediaBook("book5", 4, "Page 4"));
        listSummaries.add(new Summary(
                                        medias,
                                        "Correção do trabalho de casa do dia 3/9/2012.\nContinuação do tópico multiplicação de números racionais não negativos.\nResolução de Exercícios:",
                                        cal.getTime(), listSummaries.size()));
    }
    
    public static DataShared getInstance() {
    
        if (instance == null) {
            instance = new DataShared();
        }
        return instance;
    }
    
    public Profile getMyProfile() {
    
        return myProfile;
    }
    
    public ArrayList<Summary> getListSummaries() {
    
        return listSummaries;
    }
    
    public ArrayList<Student> getListOnline() {
    
        return slavesOn;
    }
    
    public ArrayList<Student> getListOffline() {
    
        int[] pics = { R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
                                        R.drawable.pic7, R.drawable.pic8 };
        
        for (int i = 0; i < students.size(); i++) {
            
            students.get(i).setPic(pics[i]);
        }
        
        return students;
    }
    
    public void clearAllLists() {
    
        students = creator.getTurma().getStudents();
        slavesOn = new ArrayList<Student>();
        slavesAll = new ArrayList<Student>();
        slavesAll.clear();
        slavesAll.addAll(slavesOn);
        slavesAll.addAll(students);
    }
    
    public void setListAll( ArrayList<Student> list ) {
    
        this.slavesAll = list;
    }
    
    public ArrayList<Student> getListAll() {
    
        slavesAll.clear();
        slavesAll.addAll(slavesOn);
        clearOffList();
        slavesAll.addAll(students);
        return slavesAll;
    }
    
    public ArrayList<MyCalendar> getMyCalendar() {
    
        return myCalendar;
    }
    
    private void clearOffList() {
    
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            it.next().setStatus(SlaveStatus.DISCONNECT);
        }
    }
    
    public void setMyCalendar( ArrayList<MyCalendar> myCalendar ) {
    
        this.myCalendar = myCalendar;
    }
    
    public ArrayList<ClassPeople> getClasses() {
    
        return classes;
    }
    
    public void setStudents( ArrayList<Student> connectedStudents ) {
    
        this.students = connectedStudents;
    }
    
    public void clearListConnectedStudents() {
    
        slavesOn = new ArrayList<Student>();
    }
    
    public boolean existConnectedStudents( String ipAddr ) {
    
        if (slavesOn.isEmpty()) {
            return false;
        }
        
        Iterator<Student> it = slavesOn.iterator();
        while (it.hasNext()) {
            if (it.next().getIpAdrress().equalsIgnoreCase(ipAddr)) {
                return true;
            }
        }
        
        return false;
    }
    
    public Student getConnectedStudents( String ipAddr ) {
    
        if (slavesOn.isEmpty()) {
            return null;
        }
        
        Iterator<Student> it = slavesOn.iterator();
        while (it.hasNext()) {
            Student std = it.next();
            if (std.getIpAdrress().equalsIgnoreCase(ipAddr)) {
                return std;
            }
        }
        
        return null;
    }
}
