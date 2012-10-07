
package objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class ObjectCreator {
    
    public final static int              NUM_CLASSES = 10;
    private final int                    NUM_STD     = 30;
    
    private final static String[]        classType   = { "Educação Visual e Tecnológica", "História e Geografia de Portugal",
                                    "Língua Portuguesa", "Matemática", "Ciências Físico-Químicas", "Ciências Naturais", "Educação Fisica",
                                    "Francês", " Geografia", "História", "Inglês", "Espanhol" };
    
    private final static String[]        names       = { "Patricia Almeida", "Renato Almeida", "Vitor Gonçalves", "Rui Neto",
                                    "Martinho Fernandes", "António Marante" };
    
    private static ArrayList<MyCalendar> lists;
    private static String                TAG         = "ObjectCreator";
    private ArrayList<ClassPeople>       list;
    private ClassPeople                  turma;
    
    public ObjectCreator() {
    
        makeUp(NUM_CLASSES);
        createCalendar();
    }
    
    public ClassPeople getTurma() {
    
        makeUp(0);
        return turma;
    }
    
    public void setTurma( ClassPeople turma ) {
    
        this.turma = turma;
    }
    
    public static String[] getClassType() {
    
        return classType;
    }
    
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
    
    public Profile createTeacher() {
    
        String name = "Albert Einstein";
        Profile profile = new Profile(name, new Data(), 123112456, 918761321, "test@tester.com", Sex.Masculino, new Address("Portugal",
                                        "Porto", "Boavista", "Avenida da Liberdade", 45));
        return profile;
    }
    
    /**
     * Class de teste que serve apenas para criar objectos turmas e alunos
     * 
     * @param numClasses
     * @return
     */
    private void makeUp( int numClasses ) {
    
        Random generator = new Random();
        
        list = new ArrayList<ClassPeople>();
        String[] classes = { "5ºC", "6ºB", "6ºD" };
        for (int i = 0; i < 3; i++) {
            do {
                String nameClass = classes[i];
                
                turma = new ClassPeople(nameClass, classType[generator.nextInt(classType.length)]);
                turma.addStudent(createStudent(true));
                
            }
            while (exists(turma));
            for (int k = 0; k < names.length; k++) {
                
                String nameStd = names[k];
                turma.addStudent(new Student(nameStd, new Data("12/08/2002"), 123123123, 911312312, nameStd + "@gmail.com", Sex.Masculino,
                                                new Address("Portugal", "Porto", "Boavista", "Avenida da Liberdade", 10 + k), "", 10 + k));
            }
            list.add(turma);
            turma.addStudent(createStudent(false));
            Collections.sort(list);
        }
    }
    
    private boolean exists( ClassPeople turma ) {
    
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(turma.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<ClassPeople> getClasses() {
    
        return list;
    }
    
    public ArrayList<MyCalendar> getMyCalendar() {
    
        return lists;
    }
    
    private static void createCalendar() {
    
        lists = new ArrayList<MyCalendar>();
        Calendar cal = Calendar.getInstance();
        for (int k = 1; k < 13; k++) {
            for (int i = 1; i < 32; i++) {
                
                ArrayList<HourCalendarListView> listHour = new ArrayList<HourCalendarListView>();
                for (int j = 0; j < 24; j++) {
                    listHour.add(new HourCalendarListView(new ArrayList<TextElem>()));
                }
                if (k == 2 && i >= 29) {
                    lists.add(new MyCalendar(i, k, 2012, listHour));
                    break;
                } else if ((k == 4 || k == 6 || k == 9 || k == 11) && i >= 31) {
                    lists.add(new MyCalendar(i, k, 2012, listHour));
                    break;
                } else {
                    lists.add(new MyCalendar(i, k, 2012, listHour));
                }
            }
        }
        
    }
}
