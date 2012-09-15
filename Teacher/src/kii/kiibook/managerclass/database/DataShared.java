
package kii.kiibook.managerclass.database;

import objects.Address;
import objects.ClassPeople;
import objects.Data;
import objects.MediaBook;
import objects.MyCalendar;
import objects.ObjectCreator;
import objects.Profile;
import objects.Sex;
import objects.Student;
import objects.StudentComparatorName;
import objects.Summary;
import objects.SummaryComparator;
import util.SlaveStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

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
    private ArrayList<Summary>           listSummaries = new ArrayList<Summary>();
    ArrayList<ItemSummaryListView>       events        = new ArrayList<ItemSummaryListView>();
    
    private ArrayList<ClassPeople>       list;
    private ClassPeople                  turma;
    private final static String[]        classType     = { "Educação Visual e Tecnológica", "História e Geografia de Portugal",
                                    "Língua Portuguesa", "Matemática", "Ciências Físico-Químicas", "Ciências Naturais", "Educação Fisica",
                                    "Francês", " Geografia", "História", "Inglês", "Espanhol" };
    int[]                                pics          = { R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4,
                                    R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic8 };
    
    private DataShared() {
    
        makeUp(0);
        creator = new ObjectCreator();
        myProfile = creator.createTeacher();
        myCalendar = creator.getMyCalendar();
        classes = getClasses();
        
        students = getTurma().getStudents();
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
    
    private boolean exists( ClassPeople turma ) {
    
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(turma.getName())) {
                return true;
            }
        }
        return false;
    }
    
    private void makeUp( int numClasses ) {
    
        Random generator = new Random();
        
        list = new ArrayList<ClassPeople>();
        String[] classes = { "5ºC", "6ºB", "6ºD" };
        for (int i = 0; i < 3; i++) {
            int j = 0;
            do {
                String nameClass = classes[i];
                
                turma = new ClassPeople(nameClass, classType[generator.nextInt(classType.length)]);
                Student s = createStudent(true);
                s.setPic(pics[j++]);
                turma.addStudent(s);
                
            }
            while (exists(turma));
            for (int k = 0; k < names.length; k++) {
                
                String nameStd = names[k];
                Student s = new Student(nameStd, new Data("12/08/2002"), 123123123, 911312312, nameStd + "@gmail.com", Sex.Masculino,
                                                new Address("Portugal", "Porto", "Boavista", "Avenida da Liberdade", 10 + k), "", 10 + k);
                s.setPic(pics[j++]);
                turma.addStudent(s);
            }
            list.add(turma);
            Student s = createStudent(false);
            s.setPic(pics[j++]);
            turma.addStudent(s);
            Collections.sort(list);
        }
    }
    
    private final static String[] names = { "Patricia Almeida", "Renato Almeida", "Vitor Gonçalves", "Rui Neto", "Martinho Fernandes",
                                    "António Marante" };
    
    public Student createStudent( boolean sexoFem ) {
    
        if (sexoFem) {
            String name = "Catarina Catarino";
            return new Student(name, new Data(), 123112456, 918761321, "test@tester.com", Sex.Masculino, new Address("Portugal", "Porto",
                                            "Boavista", "Avenida da Liberdade", 45), "", 1);
            
        }
        String name = "João Cabrita";
        return new Student(name, new Data(), 123112456, 918761321, "test@tester.com", Sex.Masculino, new Address("Portugal", "Porto",
                                        "Boavista", "Avenida da Liberdade", 45), "", 2);
    }
    
    public ClassPeople getTurma() {
    
        makeUp(0);
        return turma;
    }
    
    public Profile getMyProfile() {
    
        return myProfile;
    }
    
    public ArrayList<Summary> getListSummaries() {
    
        Collections.sort(listSummaries, new SummaryComparator());
        ArrayList<Summary> lista = listSummaries;
        System.out.println(lista.toString());
        Collections.sort(lista, new SummaryComparator());
        return lista;
    }
    
    public void addListSummaries( Summary sum ) {
    
        ArrayList<Summary> list = listSummaries;
        
        list.add(sum);
        Collections.sort(list, new SummaryComparator());
        listSummaries = list;
        
    }
    
    public int getIndexStudent( Student std ) {
    
        ArrayList<Student> list = getListAll();
        Collections.sort(list, new StudentComparatorName());
        for (int k = 0; k < list.size(); k++) {
            
            if (list.get(k).getName().equalsIgnoreCase(std.getName())) {
                return k;
            }
        }
        return 0;
    }
    
    public ArrayList<Student> getListOnline() {
    
        ArrayList list = slavesOn;
        Collections.sort(list, new StudentComparatorName());
        return list;
    }
    
    public ArrayList<Student> getListOffline() {
    
        ArrayList list = students;
        Collections.sort(list, new StudentComparatorName());
        return list;
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
        Collections.sort(slavesAll, new StudentComparatorName());
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
    
        return list;
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
