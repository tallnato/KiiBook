
package TestObjectsResources;

import objects.Address;
import objects.ClassPeople;
import objects.MyCalendar;
import objects.Sex;
import objects.Student;
import objects.TextElem;
import utils.Data;
import utils.HourCalendarListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class ObjectCreator {
    
    public final static int              NUM_CLASSES = 10;
    private final int                    NUM_STD     = 30;
    private final String[]               letter      = { "A", "B", "C", "D", "E", "F" };
    private final String[]               firstName   = { "Guilherme", "Gustavo", "João", "Enzo", "Lucas", "Eduardo", "Henrique", "Bruno",
                                    "Rodrigo", "Leonardo", "Diogo", "Vítor", "Carlos", "Igor", "Luis" };
    private final String[]               lastName    = { "ABREU", "ALMEIDA", "ALVES", "ANDRADE", "ANJOS", "ANTUNES", "ARAUJO", "ASSUNCAO",
                                    "AZEVEDO", "BAPTISTA", "BARBOSA", "BARROS", "BATISTA", "BORGES", "CARNEIRO", "CARVALHO", "CRUZ",
                                    "CUNHA", "DOMINGUES", "ESTEVES", "FARIA", "FIGUEIREDO", "FONSECA", "FREITAS", "GARCIA", "GASPAR",
                                    "GOMES", "GONCALVES" };
    private final static String[]        classType   = { "Educação Visual e Tecnológica", "História e Geografia de Portugal",
                                    "Língua Portuguesa", "Matemática", "Ciências Físico-Químicas", "Ciências Naturais", "Educação Fisica",
                                    "Francês", " Geografia", "História", "Inglês", "Espanhol" };
    
    private static ObjectCreator         instance;
    private static ArrayList<MyCalendar> lists;
    private static String                TAG         = "ObjectCreator";
    private ArrayList<ClassPeople>       list;
    
    private ObjectCreator( int num ) {
    
        makeUp(num);
        createCalendar();
        
    }
    
    public static String[] getClassType() {
    
        return classType;
    }
    
    public static ObjectCreator getInstance() {
    
        if (instance == null) {
            instance = new ObjectCreator(NUM_CLASSES);
        }
        return instance;
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
        
        for (int i = 0; i < numClasses; i++) {
            ClassPeople turma;
            do {
                String nameClass = (generator.nextInt(5) + 5) + "º" + letter[generator.nextInt(letter.length)];
                turma = new ClassPeople(nameClass, classType[generator.nextInt(classType.length)]);
            }
            while (exists(turma));
            for (int k = 0; k < NUM_STD; k++) {
                String first = firstName[generator.nextInt(firstName.length)];
                String nameStd = first + " " + lastName[generator.nextInt(lastName.length)];
                turma.addStudent(new Student(nameStd, new Data("12/08/2002"), 123123123, 911312312, first + "@gmail.com", Sex.Masculino,
                                                new Address("Portugal", "Porto", "Boavista", "Avenida da Liberdade", 45)));
            }
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
                for (int j = 8; j < 22; j++) {
                    listHour.add(new HourCalendarListView(j + "h", new ArrayList<TextElem>()));
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
