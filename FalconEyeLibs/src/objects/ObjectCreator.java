
package objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class ObjectCreator {
    
    public final static int              NUM_CLASSES = 10;
    private final int                    NUM_STD     = 30;
    private final String[]               firstName   = { "Guilherme", "Gustavo", "João", "Enzo", "Lucas", "Eduardo", "Henrique", "Bruno",
                                    "Rodrigo", "Leonardo", "Diogo", "Vítor", "Carlos", "Igor", "Luis" };
    private final String[]               lastName    = { "ABREU", "ALMEIDA", "ALVES", "ANDRADE", "ANJOS", "ANTUNES", "ARAUJO", "ASSUNCAO",
                                    "AZEVEDO", "BAPTISTA", "BARBOSA", "BARROS", "BATISTA", "BORGES", "CARNEIRO", "CARVALHO", "CRUZ",
                                    "CUNHA", "DOMINGUES", "ESTEVES", "FARIA", "FIGUEIREDO", "FONSECA", "FREITAS", "GARCIA", "GASPAR",
                                    "GOMES", "GONCALVES" };
    private final static String[]        classType   = { "Educação Visual e Tecnológica", "História e Geografia de Portugal",
                                    "Língua Portuguesa", "Matemática", "Ciências Físico-Químicas", "Ciências Naturais", "Educação Fisica",
                                    "Francês", " Geografia", "História", "Inglês", "Espanhol" };
    
    private static ArrayList<MyCalendar> lists;
    private static String                TAG         = "ObjectCreator";
    private ArrayList<ClassPeople>       list;
    private Student                      student;
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
            String name = "Andreia Silva";
            return new Student(name, new Data(), 123112456, 918761321, "test@tester.com", Sex.Masculino, new Address("Portugal", "Porto",
                                            "Boavista", "Avenida da Liberdade", 45), "", 1);
            
        }
        String name = "Gustavo Oliveira";
        return new Student(name, new Data(), 123112456, 918761321, "test@tester.com", Sex.Masculino, new Address("Portugal", "Porto",
                                        "Boavista", "Avenida da Liberdade", 45), "", 2);
    }
    
    public Profile createTeacher() {
    
        Random generator = new Random();
        String name = firstName[generator.nextInt(firstName.length)];
        String last = lastName[generator.nextInt(lastName.length)];
        last = last.toLowerCase();
        char c = last.charAt(0);
        c = Character.toUpperCase(c);
        
        name = name + " " + last;
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
            }
            while (exists(turma));
            for (int k = 0; k < NUM_STD; k++) {
                String first = firstName[generator.nextInt(firstName.length)];
                String last = lastName[generator.nextInt(lastName.length)];
                last = last.toLowerCase();
                char c = last.charAt(0);
                c = Character.toUpperCase(c);
                String nameStd = first + " " + last;
                turma.addStudent(new Student(nameStd, new Data("12/08/2002"), 123123123, 911312312, first + "@gmail.com", Sex.Masculino,
                                                new Address("Portugal", "Porto", "Boavista", "Avenida da Liberdade", 45)));
            }
            turma.addStudent(createStudent(true));
            turma.addStudent(createStudent(false));
            list.add(turma);
            
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
