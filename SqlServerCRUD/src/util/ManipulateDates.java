package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de conversões de tipos de datas, formatos e validaçoes
 * @author CAIO
 */
public class ManipulateDates {
    
    
    //Verificar se a data é valida conforme o formato
    public static boolean isValidDate(String dateString, String dateFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        
        try {
          Date data = sdf.parse(dateString);
          //System.out.println(new ManipulateDates().utilDateToStringDate(data, "dd/MM/yyyy"));
          // se passou pra cá, é porque a data é válida
          return true;
        } catch(ParseException e) {
          // se cair aqui, a data é inválida
          //System.err.println("Data inválida");
          return false;
        }
    }
    
    //Passe a data em string e o formato que ela está
    public java.sql.Date stringToSqlDate(String dateString, String dateFormat)
    {   
        if(dateString == null || "".equals(dateString))
            return null;
        
        if(dateFormat == null)
            dateFormat = "dd/MM/yyyy";
        try {
            //dateString = 24/05/1996, 24-05-1996, 19962405 ...
            //dateFormat = dd/MM/yyyy, dd-MM-yyyy, yyyyMMdd ...            
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            java.util.Date utilDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            return sqlDate;
        } catch (ParseException ex) {
            return null;
        }
    }
    
    //Passe a data em string e o formato que ela está
    public java.util.Date stringToUtilDate(String dateString, String dateFormat)
    {
        if(dateString == null || dateString.equals(""))
        if(dateFormat == null)
            dateFormat = "dd/MM/yyyy";        
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            java.util.Date utilDate = format.parse(dateString);
            return utilDate;
        } catch (ParseException ex) {
            Logger.getLogger(ManipulateDates.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //Passe a data do Java (da biblioteca java.util.Date)
    public java.sql.Date utilDateToSqlDate(java.util.Date utilDate)
    {
        java.sql.Date sqlDate = null;
        if(utilDate != null)
            sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }
    
    //Passe a data do Sql (da biblioteca java.sql.Date)
    public java.util.Date sqlDateToUtilDate(java.sql.Date sqlDate)
    {
        java.util.Date utilDate = null;
        if(sqlDate != null)
            utilDate = new java.util.Date(sqlDate.getTime());        
        return utilDate;
    }
    
    //Passe a data do sql, e o formato que deseja obter em string
    public String sqlDateToStringDate(java.sql.Date sqlDate, String stringFormatWanted)
    {        
        if(sqlDate == null)
            return null; //or "";
        
        if(stringFormatWanted == null)
            stringFormatWanted = "dd/MM/yyyy";        
        
        //stringFormatWanted = "MM/dd/yyyy" , "dd/MM/yyyy"...
        DateFormat df = new SimpleDateFormat(stringFormatWanted);
        String dateText = df.format(sqlDate);
        return dateText;        
    }
    
    //Passe a data do java e o formato que deseja receber na string
    public String utilDateToStringDate(java.util.Date utilDate, String stringFormatWanted)
    {
        if(utilDate == null)
            return null; //or "";
        
        if(stringFormatWanted == null)
            stringFormatWanted = "dd/MM/yyyy";
        
        DateFormat df = new SimpleDateFormat(stringFormatWanted);
        String dateText = df.format(utilDate);
        return dateText;
    }
    
    public String stringDateToNewStringDateFormat(String dateStr, String formatInput, String formatOutput)
    {
        DateFormat fromFormat = new SimpleDateFormat(formatInput); //ex "yyyy-MM-dd"
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat(formatOutput); //"dd-MM-yyyy"
        toFormat.setLenient(false);
        Date date;
        try {
            date = fromFormat.parse(dateStr);
            return toFormat.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(ManipulateDates.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
