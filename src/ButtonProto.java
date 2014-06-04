import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;

public class ButtonProto extends Applet implements ActionListener {
	TextField fyear, fmonth;
	Button button;
	int count = 0;
	public void init() {
		fyear = new TextField("", 4);
		add(fyear);
		fmonth = new TextField("", 2);
		add(fmonth);
		
		button = new Button("計算");
		button.addActionListener(this);
		add(button);
	}
	public void paint(Graphics g) {
		g.drawString(String.valueOf(count), 80, 100);
		}
	public void actionPerformed(ActionEvent e) {
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		int nowMonth = now.get(Calendar.MONTH) + 1;
		int birthYear = Integer.parseInt(fyear.getText());
		int birthMonth = Integer.parseInt(fmonth.getText());
		if (birthMonth > nowMonth)
			count =	nowYear-birthYear - 1;
		else
			count = nowYear - birthYear;
		repaint();
	}
}
