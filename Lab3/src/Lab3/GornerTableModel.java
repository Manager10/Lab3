package Lab3;

import javax.swing.table.AbstractTableModel;

import java.text.NumberFormat;

public class GornerTableModel  extends AbstractTableModel
{
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private double result[] = new double[3];
    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
// В данной модели два столбца
        return 3;
    }
    public int getRowCount()
    {
// Вычислить количество точек между началом и концом отрезка
// исходя из шага табулирования
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col)
    {
        double x = from + step*row;
        switch(col)
        {
            case 0:
                return x;
            case 1:
            {
                result[0] = 0.0;
                for(int i = 0; i < coefficients.length; i++)
                {
                    result[0] += Math.pow(x, coefficients.length-1-i)*coefficients[i];
                }
                return result[0];
            }
            default:
                int temp1 = (int)result[0];
                int temp2 = (int)(result[0] * 10);
                if (temp1 % 10 == temp2 % 10)
                    return true;
                else
                    return false;
        }
    }
    public String getColumnName(int col)
    {
        switch (col)
        {
            case 0:
// Название 1-го столбца
                return "Значение X";
            case 1:
// Название 2-го столбца
                return "Значение многочлена";
            default:
                return "Ограниченная симметрия";
        }
    }
    public Class<?> getColumnClass(int col)
    {
        if (col == 2)
            return Boolean.class;
        else
        return Double.class;
    }
}
