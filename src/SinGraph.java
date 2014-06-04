import java.awt.*;
import java.applet.*;

//sinのグラフを書くアプレット
public class SinGraph extends Applet{
	public void paint( Graphics g ){
		int digree;		//角度　forのカウンタ変数
		int yp, yv; 	//yp:一個前のデータのy座標  yv:今のデータのy座標
		double value; 	//今の角度に対するsinの値
		double prev; 	//一つ前のsinの値
		
		//グラフの軸を描画
		g.drawLine(50, 50, 50, 150);	//縦線
		g.drawLine(50, 100, 410, 100);	//横線
		g.drawString("0", 40, 105);
		g.drawString("1", 40, 55);
		g.drawString("-1", 35, 155);

		g.setColor(Color.red);		//グラフを赤で書く
		prev = Math.sin(0);	//0度の時の値をprevにセット
		for( digree = 1; digree <= 360; digree++){ //角度1度から360度
			yp = (int)(100-50*prev);	// 値:y座標　0:100 1:50 -1:150

			//今の角度に対するsin値を取得
			value = Math.sin(Math.toRadians(digree)); 	
			//Java標準のMath.sin()は引数をラジアンで指定
			//しなければならない

			yv = (int)(100-50*value);		// 値:y座標　0:100 1:50 -1:150
			g.drawLine(50+digree-1, yp, 50+digree, yv); //線を引く
			prev = value;	//今の値を変数prevに保存
		}
	}
}



