import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

class CalculatorPanel extends JPanel {
    private JTextField first;
    private JTextField second;
    private JComboBox operator;

    String checkInput="[+-]?[0-9]*[[0-9][0-9][0-9][ ]?]*+[.,]?[0-9]*";

    private JTextField display;
    private JPanel panel;
    private boolean start;
    public CalculatorPanel() {
        setLayout(new BorderLayout());

        JButton name = new JButton("Карань Дариан Александрович 4 курс 4 группа 2022");
        name.setEnabled(false);
        name.setFont(name.getFont().deriveFont(15f));
        add(name, BorderLayout.SOUTH);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(display.getFont().deriveFont(50f));
        add(display, BorderLayout.NORTH);
        ActionListener result = new ResultAction();
        panel = new JPanel();

        panel.setLayout(new GridLayout(5, 5));
        first = new JTextField("0");
        second = new JTextField("0");

        String[] operators = {"+", "-","*","/"};
        operator = new JComboBox(operators);
        operator.setSelectedIndex(0);

        panel.add(first);
        panel.add(operator);
        panel.add(second);
        addButton("=",result);


        add(panel, BorderLayout.CENTER);

    }
    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(20f));
        button.addActionListener(listener);
        panel.add(button);
    }

    private class ResultAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if( first.getText().matches(checkInput) && second.getText().matches(checkInput)){

                String pattern = "#,###,###,###,###.#############";
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setDecimalSeparator('.');
                dfs.setGroupingSeparator(' ');
                DecimalFormat decimalFormat = new DecimalFormat(pattern, dfs);
                decimalFormat.setParseBigDecimal(true);

                BigDecimal firstN = null;
                BigDecimal secondN = null;
                try {
                    firstN = (BigDecimal) decimalFormat.parse(first.getText().replace(',','.'));
                    secondN = (BigDecimal) decimalFormat.parse(second.getText().replace(',','.'));
                } catch (ParseException e) {
                    e.printStackTrace();
                };

                if( decimalFormat.format(firstN).equals(first.getText().replace(',','.'))
                || decimalFormat.format(secondN).equals(second.getText().replace(',','.'))){

                    //BigDecimal firstN = new BigDecimal(first.getText().replace(',','.'));
                    //BigDecimal secondN = new BigDecimal(second.getText().replace(',','.'));

                    BigDecimal result = null;
                    String command = (String)operator.getSelectedItem();
                    switch (command) {
                        case "+":
                            result = firstN.add(secondN);
                            break;
                        case "-":
                            result = firstN.subtract(secondN);
                            break;
                        case "*":
                            result = firstN.multiply(secondN);
                            break;
                        case "/":
                            if(secondN.compareTo(BigDecimal.ZERO) == 0){
                                display.setText("One of us wrote something wrong");

                                break;
                            }
                            result = firstN.divide(secondN, new MathContext(50, RoundingMode.HALF_UP));
                            result = result.setScale(10, RoundingMode.HALF_UP);
                            break;
                    }
                    if (result.compareTo(BigDecimal.ZERO) == 0) {
                        result = BigDecimal.ZERO;
                    }

                    /*dfs.setDecimalSeparator('.');
                    decimalFormat = new DecimalFormat(pattern, dfs);*/
                    String format = decimalFormat.format(result);
                    display.setText(format);
                    //display.setText(result.toPlainString());
                }
                else{
                    display.setText("One of us wrote something wrong");
                }
            }
            else{
                display.setText("One of us wrote something wrong");
            }

            /*System.out.println(firstN);
            System.out.println(secondN);
            System.out.println(result.toString());*/

        }
    }

}