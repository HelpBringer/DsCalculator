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
    private JTextField third;
    private JTextField fourth;
    private JComboBox operator1;
    private JComboBox operator2;
    private JComboBox operator3;

    private JComboBox roundType;

    String checkInput="[+-]?[0-9]*[[0-9][0-9][0-9][ ]?]*+[.,]?[0-9]*";

    private JTextField display;
    private JTextField rounder;
    private JPanel panelRes;
    private JPanel panel;
    private JPanel panel1;
    private JPanel resultP;
    private JPanel group;
    private boolean start;
    public CalculatorPanel() {
        setLayout(new BorderLayout());

        JButton name = new JButton("Карань Дариан Александрович 4 курс 4 группа 2022");
        name.setEnabled(false);
        name.setFont(name.getFont().deriveFont(15f));
        JButton leftP = new JButton("(");
        leftP.setEnabled(false);
        leftP.setFont(leftP.getFont().deriveFont(15f));
        JButton rightP = new JButton(")");
        rightP.setEnabled(false);
        rightP.setFont(rightP.getFont().deriveFont(15f));

        add(name, BorderLayout.SOUTH);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(display.getFont().deriveFont(50f));
        //add(display, BorderLayout.NORTH);
        panelRes = new JPanel();
        panelRes.setLayout(new GridLayout(1,1));
        panelRes.add(display);

        resultP = new JPanel();
        resultP.setLayout(new GridLayout(1,3));
        JButton roundText = new JButton("Округление");
        roundText.setEnabled(false);
        roundText.setFont(roundText.getFont().deriveFont(15f));
        rounder = new JTextField("0");
        rounder.setEditable(false);
        rounder.setFont(rounder.getFont().deriveFont(15f));
        String[] roundTypes = {"Математическое","Банковское","Усечение"};
        roundType = new JComboBox(roundTypes);
        resultP.add(roundText);
        resultP.add(rounder);
        resultP.add(roundType);
        //add(rounder, BorderLayout.NORTH);


        ActionListener result = new ResultAction();
        panel = new JPanel();

        panel.setLayout(new GridLayout(6, 5));
        first = new JTextField("0");
        second = new JTextField("0");
        third = new JTextField("0");
        fourth = new JTextField("0");

        String[] operators = {"+", "-","*","/"};
        operator1 = new JComboBox(operators);
        operator1.setSelectedIndex(0);
        operator2 = new JComboBox(operators);
        operator2.setSelectedIndex(0);
        operator3 = new JComboBox(operators);
        operator3.setSelectedIndex(0);

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1,5));
        panel1.add(leftP);
        panel1.add(second);
        panel1.add(operator2);
        panel1.add(third);
        panel1.add(rightP);

        panel.add(first);
        panel.add(operator1);
        panel.add(panel1);
        panel.add(operator3);
        panel.add(fourth);

        addButton("=",result);

        group = new JPanel();
        group.setLayout(new GridLayout(2,1));
        group.add(panelRes);
        group.add(resultP);
        //add(panelRes, BorderLayout.NORTH);
        //add(resultP, BorderLayout.NORTH);
        add(group,BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

    }
    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(20f));
        button.addActionListener(listener);
        panel.add(button);
    }

    private class ResultAction implements ActionListener {

        public BigDecimal operation(BigDecimal firstN, BigDecimal secondN, String command){
            BigDecimal result= new BigDecimal("0");
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
            return result;


        }

        public void actionPerformed(ActionEvent event) {
            if( first.getText().matches(checkInput) && second.getText().matches(checkInput)
                    && third.getText().matches(checkInput) && fourth.getText().matches(checkInput)){

                String pattern = "#,###,###,###,###.#############";
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setDecimalSeparator('.');
                dfs.setGroupingSeparator(' ');
                DecimalFormat decimalFormat = new DecimalFormat(pattern, dfs);
                decimalFormat.setParseBigDecimal(true);

                BigDecimal firstN = null;
                BigDecimal secondN = null;
                BigDecimal thirdN = null;
                BigDecimal fourthN = null;
                try {
                    firstN = (BigDecimal) decimalFormat.parse(first.getText().replace(',','.'));
                    secondN = (BigDecimal) decimalFormat.parse(second.getText().replace(',','.'));
                    thirdN = (BigDecimal) decimalFormat.parse(third.getText().replace(',','.'));
                    fourthN = (BigDecimal) decimalFormat.parse(fourth.getText().replace(',','.'));
                } catch (ParseException e) {
                    e.printStackTrace();
                };

                if( decimalFormat.format(firstN).equals(first.getText().replace(',','.'))
                || decimalFormat.format(secondN).equals(second.getText().replace(',','.'))
                || decimalFormat.format(thirdN).equals(third.getText().replace(',','.'))
                ||decimalFormat.format(fourthN).equals(fourth.getText().replace(',','.'))){

                    BigDecimal result = null;
                    BigDecimal tmp = null;
                    String command2 = (String) operator2.getSelectedItem();
                    result = operation(secondN, thirdN, command2);
                    System.out.println(result);
                    String command1 = (String) operator1.getSelectedItem();
                    String command3 = (String) operator3.getSelectedItem();
                    if( (command1.equals("-") || command1.equals("+")) &&
                            (command3.equals("*") || command3.equals("/"))){
                        System.out.println(result);
                        result = operation(result,fourthN,command3);
                        System.out.println(result);
                        result = operation(firstN,result,command1);
                    }
                    else{
                        System.out.println(result);
                        result = operation(firstN,result,command1);
                        System.out.println(result);
                        result = operation(result,fourthN,command3);
                    }


                    String format = decimalFormat.format(result);

                    String rType = (String) roundType.getSelectedItem();
                    BigDecimal roundResult = null;
                    switch (rType) {
                        case "Математическое":
                            roundResult = result.setScale(0, RoundingMode.HALF_UP);
                            break;
                        case "Банковское":
                            roundResult = result.setScale(0, RoundingMode.HALF_EVEN);
                            break;
                        case "Усечение":
                            roundResult = result.setScale(0, RoundingMode.HALF_DOWN);
                            break;
                    }
                    String formatR = decimalFormat.format(roundResult);

                    rounder.setText(formatR);
                    display.setText(format);

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