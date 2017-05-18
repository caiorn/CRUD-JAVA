package model;

import connection.SqlServerConnection;
import java.sql.Connection;
import java.sql.Date;//usar java.sql.Date ao inves de java.util.Date por conta de problemas de conversão
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.ManipulateDates;
import view.frmMain;


/**
 *
 * @author CAIO
 */
public final class Pessoa {
    //Fields
    private int id;
    private String nome;
    private char sexo;
    private Date dataNascimento;
    private boolean empregado;
    private double salario;
    private String nomeFoto;
    
    //Constructor
    public Pessoa(String nome, char sexo)
    {
        //campos obrigatorios(NOT NULL) no bd
        setNome(nome);
        setSexo(sexo);        
    }
    
    public Pessoa()
    {
        
    }
    
    //Encapsulation        
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        nome = nome.trim().replaceAll("\\s+"," ").toLowerCase();
        String[] parts = nome.split(" ");
        nome = "";
        for(String partNome : parts)
        {
            partNome = partNome.substring(0,1).toUpperCase().concat(partNome.substring(1));//deixa a 1 palavra em maisucula
            nome += partNome + " ";
        }      
        this.nome = nome.trim();        
        //Input  " cAio   souZA tesT   "
        //Output "Caio Souza Test"   
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    
    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isEmpregado() {
        return empregado;
    }

    public void setEmpregado(boolean empregado) {
        this.empregado = empregado;
    }
    
    public double getSalario() {
        return salario;        
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public void setNomeFoto(String nomeFoto) {
        if(nomeFoto.endsWith(".png"))
            this.nomeFoto = nomeFoto;
        else if(!nomeFoto.equals(""))
            this.nomeFoto = nomeFoto +".png";
        else
            this.nomeFoto = nomeFoto;
    }
    
    //Methods
    public void Inserir()
    {
        try {
            Connection con =  new SqlServerConnection().getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Pessoa (Nome, Sexo, DataNascimento, Empregado, Salario, Foto) VALUES (?, ?, ?, ?, ?, ?)");
            //adicionando os parametros
            pstmt.setString(1, this.getNome());
            pstmt.setString(2, String.valueOf(this.getSexo()));
            pstmt.setDate(3, this.getDataNascimento()); //pstmt.setString(3, "24/05/1996");
            pstmt.setBoolean(4, this.isEmpregado());            
            if(this.getSalario() == 0)
                pstmt.setNull(5, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(5, this.getSalario());            
            pstmt.setString(6, this.getNomeFoto());
            //Executando query e libera da memoria
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Inserindo com Sucesso");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void Alterar()
    {
        try {
            Connection con =  new SqlServerConnection().getConnection();
            PreparedStatement pstmt = con.prepareStatement("UPDATE Pessoa SET Nome = ?, Sexo = ?, DataNascimento = ?, Empregado = ?, Salario = ?, Foto = ? WHERE Id_P = ?");
            //adicionando os parametros
            pstmt.setString(1, this.getNome());
            pstmt.setString(2, String.valueOf(this.getSexo()));
            pstmt.setDate(3, this.getDataNascimento()); //pstmt.setString(3, "24/05/1996");
            pstmt.setBoolean(4, this.isEmpregado());            
            if(this.getSalario() == 0)
                pstmt.setNull(5, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(5, this.getSalario());            
            pstmt.setString(6, this.getNomeFoto());
            pstmt.setInt(7, this.getId());
            //Executando query e libera da memoria
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Alterado com Sucesso");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void Excluir(int[] ids)
    {
        String parametros = "(";
        for(int id: ids){        
            parametros += id+",";
        }
        parametros = parametros.substring(0, parametros.lastIndexOf(',')) + ")";
        try (Statement stmt = new SqlServerConnection().getConnection().createStatement()) {
            stmt.executeUpdate("DELETE from Pessoa where Id_P IN "+parametros);
            stmt.close();            
            JOptionPane.showMessageDialog(null, "Excluido com Sucesso");            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void CarregarPessoasTabela(BuscarPor tipoBusca, String valor, DefaultTableModel tabela)
    {   
        //Limpa os dados da tabela
        tabela.setRowCount(0);
        valor = tipoBusca == BuscarPor.Nome ? "%"+valor+"%" : valor; //ex: '%name%' or 1 or '1996-05-24'
        
        try {
            Connection con =  new SqlServerConnection().getConnection();
            PreparedStatement pstmt = con.prepareStatement(tipoBusca.getQuery()); //"SELECT * FROM Pessoa WHERE Nome Like ?"
            pstmt.setString(1, valor); 

            ResultSet rs = pstmt.executeQuery();
            
            NumberFormat formatoReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));            
            ManipulateDates converteData = new ManipulateDates();
            while(rs.next())
            {   
                String dataFormatada = converteData.sqlDateToStringDate(rs.getDate(4), "dd/MM/yyyy");
                
                //Adiciona uma nova linha na tabela com os valores
                tabela.addRow(new Object[]
                    { 
                    rs.getInt(1),       //Id_P
                    rs.getString(2),    //Nome
                    rs.getString(3),    //Sexo
                    dataFormatada,   //DataNascimento string (dd/MM/yyyy)
//                    rs.getDate(4),      //DataNascimento date(yyyy-MM-dd)
                    rs.getBoolean(5) == true ? "Sim": "Não" , //Empregado
                    formatoReal.format(rs.getDouble(6)),    //Salario
                    rs.getString(7)     //Nome da foto
                    }
                );
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static Integer BuscarProxIdCadastro()
    {
        try {
            PreparedStatement pstmt = new SqlServerConnection().getConnection().prepareStatement("SELECT IDENT_CURRENT('Pessoa') + IDENT_INCR('Pessoa')");
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);           
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
