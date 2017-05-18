package model;

/**
 *
 * @author CAIO
 */
public enum BuscarPor {
    Id("Id_P"), Nome("Nome"),DataDeNascimento("DataNascimento");
    
    //Encapsulamento
    private final String query;
    public String getQuery()
    {
        return query;
    }
    
    //Construtor
    BuscarPor(String keyWhere)
    {                
        query = "SELECT * FROM Pessoa WHERE " + keyWhere + " LIKE ?";
    }
}
