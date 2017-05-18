import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author CAIO
 */
public class testMain {
    public static void main(String[] args) {
        
        //JOptionPane personalizado
        JComboBox jc = new JComboBox(new Object[]{"item 1", "item 2", "item 3"});
        int op = JOptionPane.showConfirmDialog(null, jc , "Selecione", JOptionPane.OK_CANCEL_OPTION);
        if(op == JOptionPane.OK_OPTION){
                JOptionPane.showMessageDialog(null, jc.getSelectedItem().toString());
        }        
    }
}
