package org.netbeans.modules.codegenerator.color;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.netbeans.spi.editor.codegen.CodeGeneratorContextProvider;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * https://colorchooser.dev.java.net/
 * @author INCH Team
 */
public class ColorCodeGenerator implements CodeGenerator {

    JTextComponent textComp;

    /**
     * 
     * @param context containing JTextComponent and possibly other items registered by {@link CodeGeneratorContextProvider}
     */
    private ColorCodeGenerator(Lookup context) { // Good practice is not to save Lookup outside ctor
        textComp = context.lookup(JTextComponent.class);
    }

    public static class Factory implements CodeGenerator.Factory {

        public List<? extends CodeGenerator> create(Lookup context) {
            return Collections.singletonList(new ColorCodeGenerator(context));
        }
    }

    /**
     * The name which will be inserted inside Insert Code dialog
     */
    public String getDisplayName() {
        return NbBundle.getMessage(this.getClass(), "display-name");
    }

    private String formatColorCode(Color color) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("new Color(");
        buffer.append(color.getRed()).append(",").append(color.getGreen()).append(",").append(color.getBlue());
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * This will be invoked when user chooses this Generator from Insert Code
     * dialog
     */
    public void invoke() {
        try {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(colorChooser, NbBundle.getMessage(this.getClass(), "dialog-title"),
                                                   new Color(100, 255, 100));

            if (color != null) {
                Document doc = textComp.getDocument();
                int cursor = textComp.getCaret().getDot();
                String selection = textComp.getSelectedText();
                if (selection != null) {
                    doc.remove(cursor, selection.length());
                }
                doc.insertString(cursor, formatColorCode(color), null);
                textComp.requestFocus();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
