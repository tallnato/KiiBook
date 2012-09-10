
package objects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author Rui Neto
 */
public class Data implements Serializable {
    
    private static final long serialVersionUID = -6548843282960499555L;
    
    private final int         dia;
    private final int         mes;
    private final int         ano;
    
    public Data( int dia, int mes, int ano ) {
    
        if (!isValid(dia, mes, ano)) {
            throw new IllegalArgumentException("Formato da data incorrecto");
        }
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }
    
    public Data( String data ) {
    
        String[] valores = data.split("/");
        
        assert (valores.length == 3);
        
        int day = Integer.valueOf(valores[0]);
        int mouth = Integer.valueOf(valores[1]);
        int year = Integer.valueOf(valores[2]);
        
        ano = year;
        mes = mouth;
        dia = day;
        
    }
    
    public Data() {
    
        Data data;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        data = dataSplitBarra(sdf.format(cal.getTime()));
        ano = data.getAno();
        dia = data.getDia();
        mes = data.getMes();
        
    }
    
    private boolean isValid( int dia, int mes, int ano ) {
    
        String inDate = ano + "-";
        if (mes < 10) {
            inDate += "0" + mes + "-";
        } else {
            inDate += mes + "-";
        }
        if (dia < 10) {
            inDate += "0" + dia;
        } else {
            inDate += dia;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        
        if (inDate.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }
        
        dateFormat.setLenient(false);
        
        try {
            dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }
    
    /**
     * @return the dia
     */
    public int getDia() {
    
        return dia;
    }
    
    /**
     * @return the mes
     */
    public int getMes() {
    
        return mes;
    }
    
    /**
     * @return the ano
     */
    public int getAno() {
    
        return ano;
    }
    
    @Override
    public String toString() {
    
        return getDia() + "-" + getMes() + "-" + getAno();
    }
    
    private static Data dataSplitBarra( String texto ) {
    
        assert (texto != null);
        String[] valores = texto.split("-");
        
        assert (valores.length == 3);
        
        int dia = Integer.valueOf(valores[0]);
        int mes = Integer.valueOf(valores[1]);
        int ano = Integer.valueOf(valores[2]);
        Data data = new Data(dia, mes, ano);
        return data;
    }
    
    public static String dataExtensive( int day, int month, int year ) {
    
        String str = day + " de ";
        switch (month) {
            case 1:
                str += "Janeiro de ";
                break;
            case 2:
                str += "Fevereiro de ";
                break;
            case 3:
                str += "Março de ";
                break;
            case 4:
                str += "Abril de ";
                break;
            case 5:
                str += "Maio de ";
                break;
            case 6:
                str += "Junho de ";
                break;
            case 7:
                str += "Julho de ";
                break;
            case 8:
                str += "Agosto de ";
                break;
            case 9:
                str += "Setembro de ";
                break;
            case 10:
                str += "Outubro de ";
                break;
            case 11:
                str += "Novembro de ";
                break;
            case 12:
                str += "Dezembro de ";
                break;
        }
        
        str += year;
        
        return str;
    }
    
    public static String dataExtensive( Data data ) {
    
        int day = data.getDia();
        int month = data.getMes();
        int year = data.getAno();
        
        String str = day + " de ";
        switch (month) {
            case 1:
                str += "Janeiro de ";
                break;
            case 2:
                str += "Fevereiro de ";
                break;
            case 3:
                str += "Março de ";
                break;
            case 4:
                str += "Abril de ";
                break;
            case 5:
                str += "Maio de ";
                break;
            case 6:
                str += "Junho de ";
                break;
            case 7:
                str += "Julho de ";
                break;
            case 8:
                str += "Agosto de ";
                break;
            case 9:
                str += "Setembro de ";
                break;
            case 10:
                str += "Outubro de ";
                break;
            case 11:
                str += "Novembro de ";
                break;
            case 12:
                str += "Dezembro de ";
                break;
        }
        
        str += year;
        
        return str;
    }
}
