import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener
{
    JFrame frame;
    JPanel scrollPanel, boxPanel, connectPanel, buttonPanel, labelPanel;
    JLabel rl, cl, zl, sl, bl;
    double realValue = 0;
    double complexValue = 0;
    double zoomValue = 1;
    float saturationValue=0.5f;
    JScrollBar realBar, complexBar, zoomBar, satBar, brightnessBar;
    float brightnessValue = 0.75f;
    JCheckBox[] boxes;
    JButton resetButton;
    double nVal=1.0;
    JTextField nField;
    int width = 1500, height = 1000;
    BufferedImage image;
    float maxIter = 60.0f;
    public JuliaSetProgram()
    {
        frame = new JFrame("Julia Set Program");
        frame.add(this);
        realBar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-20000,20000);
        realBar.addAdjustmentListener(this);
        complexBar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-20000,20000);
        complexBar.addAdjustmentListener(this);
        zoomBar = new JScrollBar(JScrollBar.HORIZONTAL,10000,0,10000,100000);
        zoomBar.addAdjustmentListener(this);
        satBar = new JScrollBar(JScrollBar.HORIZONTAL,5000,0,0,10000);
        satBar.addAdjustmentListener(this);
        brightnessBar = new JScrollBar(JScrollBar.HORIZONTAL,75000,0,0,100000);
        brightnessBar.addAdjustmentListener(this);
        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(5, 1));
        scrollPanel.add(realBar);
        scrollPanel.add(complexBar);
        scrollPanel.add(zoomBar);
        scrollPanel.add(satBar);
        scrollPanel.add(brightnessBar);
        /*boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(3, 1));
        boxes = new JCheckBox[3];
        for(int x=0;x<boxes.length;x++)
        {
             boxes[x] = new JCheckBox();
             boxes[x].addActionListener(this);
             boxPanel.add(boxes[x]);
        }*/
        connectPanel = new JPanel();
        connectPanel.setLayout(new BorderLayout());
        connectPanel.add(scrollPanel, BorderLayout.CENTER);
        rl = new JLabel("Real Value: "+realValue);
        cl = new JLabel("Complex Value: "+complexValue);
        zl = new JLabel("Zoom: "+zoomValue);
        sl = new JLabel("Saturation: "+saturationValue);
        bl = new JLabel("Brightness: "+brightnessValue);
        labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(5, 1));
        labelPanel.add(rl);
        labelPanel.add(cl);
        labelPanel.add(zl);
        labelPanel.add(sl);
        labelPanel.add(bl);
        connectPanel.add(labelPanel, BorderLayout.WEST);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        resetButton = new JButton("Reset to Default");
        resetButton.addActionListener(this);
        JLabel fieldLabel = new JLabel("Enter n-value:");
        nField = new JTextField();
        nField.setText(""+nVal);
        nField.addActionListener(this);
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1, 2));
        textPanel.add(fieldLabel);
        textPanel.add(nField);
        buttonPanel.add(textPanel, BorderLayout.WEST);
        buttonPanel.add(resetButton, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.NORTH);
        //connectPanel.add(boxPanel, BorderLayout.EAST);
        frame.add(connectPanel, BorderLayout.SOUTH);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); //erases board every time we call paintComponent()
        g.drawImage(makeJuliaFunc(), 0, 0, null);
    }
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource() == realBar)
        {
            realValue = realBar.getValue()/10000.0;
            rl.setText("Real Value: "+realValue);
        }
        if(e.getSource() == complexBar)
        {
            complexValue = complexBar.getValue()/10000.0;
            cl.setText("Complex Value: "+complexValue);
        }
        if(e.getSource() == zoomBar)
        {
            zoomValue = zoomBar.getValue()/10000.0;
            zl.setText("Zoom: "+zoomValue);
        }
        if(e.getSource() == satBar)
        {
            saturationValue = satBar.getValue()/10000.0f;
            sl.setText("Saturation: "+saturationValue);
        }
        if(e.getSource()==brightnessBar)
        {
            brightnessValue = brightnessBar.getValue()/(100000.0f);
            bl.setText("Brightness: "+brightnessValue);
        }
        repaint();
    }
    public void actionPerformed(ActionEvent e)
    {
        /*if(boxes[0].isSelected())
            realValue = 0;
        else realValue = realBar.getValue();*/
        if(e.getSource()==resetButton)
        {
            brightnessBar.setValue(75000);
            satBar.setValue(5000);
            realBar.setValue(0);
            complexBar.setValue(0);
            zoomBar.setValue(10000);
            nVal = 1.0;
            nField.setText(""+nVal);
        }
        if(e.getSource()==nField)
        {
            nVal = Double.parseDouble(nField.getText().trim());
        }
        repaint();
    }
    public BufferedImage makeJuliaFunc()
    {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double zx=0, zy=0;
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {     
                float i = maxIter;
                zx = 1.5* (x-(0.5*width))/(0.5*zoomValue*width);
                zy = (y-(0.5*height))/(0.5*zoomValue*height);
                while(zx*zx + zy*zy<6 && i>0)
                {
                    double d = (zx*zx-zy*zy)+realValue;
                    zy = 2*zx*zy + complexValue;
                    zx = d;
                    zy = Math.pow(zy, nVal);
                    zx = Math.pow(zx, nVal);
                    i--;
                }
                int c=0;
                if(i>0)
                    c = Color.HSBtoRGB((maxIter/i)%1,saturationValue,brightnessValue);
                else c = Color.HSBtoRGB(maxIter/i,saturationValue, 0);
                image.setRGB(x, y, c);
            }
        }
        return image;
    }
    public static void main(String[] args)
    {
        JuliaSetProgram app = new JuliaSetProgram();
    }
}