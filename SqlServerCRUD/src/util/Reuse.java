package util;

import java.awt.Component;
import java.awt.Container;
import java.text.DecimalFormat;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
/**
 *
 * @author CAIO
 */
public final class Reuse {
    
    //private constructor empty
    private Reuse() {  };
    
    //limpa todos componentes a partir de determinado controle
    public static void clearAll(Container container) {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JFormattedTextField) {
                JFormattedTextField field = (JFormattedTextField) component;
                field.setValue(null);
            } else if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setText("");
            } else if (component instanceof Container) {
                clearAll((Container) component);
            }
        }
    }
    
    //habilita/desabilita todos componentes dentro de um Panel,Form ou outro container
    public static void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }
    
    //retorna o texto do radioButton Selecionado que pertence a um ButtonGroup
    public static String getSelectedButtonText(ButtonGroup buttonGroup) {
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
        AbstractButton button = buttons.nextElement();
        if (button.isSelected()) {
            return button.getText();
        }
    }
    return null;
    }
            
    public static <T> boolean oneOfEquals(T expected, T... os) {
        for (T o : os) {
            if (expected.equals(o)) return true;
        }
        return false;
        //oneOfEquals(btnClicked, btnAlterar, btnNovo, btnConsultar)
    }        
}

/*
regex
String onlyNumberAndDot = txt.getText().trim().replaceAll("[^-?0-9\\.]+", "");

lançamento Excessoes
public void fazQualquerCoisa(int natural) {
    if (natural < 0)
        throw new IllegalArgumentException("Números naturais não podem ser negativos!");
    //resto do método aqui
}
*/

