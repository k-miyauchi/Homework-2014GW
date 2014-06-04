
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.JOptionPane;

//身体、感情、知性について、誕生日と現在の日付に基づいて今後60日分計算してくれるコード。

public class Biorhythm extends Applet implements ActionListener,MouseMotionListener {
	//したごしらえ
	//あまりやべー数字を入れると大変なことになるので注意
	TextField fyear, fmonth, fday, fcalcdays; //接頭語field付きのハンガリアン記法？　教科書まんま
	Button button; //教科書まんま
	final int xOffset = 50; //グラフ用基準座標
	final int yOffset = 50; //グラフ用基準座標
	final int yLen = 100; //グラフ用縦長さ
	final int xLen = 6; //グラフの横長用 *daysが実際の長さ
	final int[] bioLen = {23, 28, 33}; //周期　要素増やせるけど色とか対応してないので自分で足せ。。
	int days = 60; //何日むこうまでやるか 設定しただけグラフを横に伸ばしたり縮めたりするので、2000日分出そうとしてもモニターの限界が。
	int passedDay = 0;	//	経過日数
	double[] bio = new double[days]; //バイオリズム計算用
	Dimension size;	//ウィンドウサイズ取得用
	Image back;	//バッファ
	Graphics buffer;	//バッファ
	boolean isDraw = false; 	//描画するかのフラグ
	boolean isEnter = false;	//マウス
	boolean pvInit = false;	//最初バッファにグラフの軸を入れる用
	boolean graphInit = false; //グラフの初期化フラグ
	boolean illegalValueError = false; //埋め込まれた数値が異常じゃないか
	Rectangle area = new Rectangle(xOffset, yOffset, xLen * days, yLen);	//グラフ描画領域
	int xPoint = 0; //マウス位置取得用
	int yPoint = 0; //マウス位置取得用

	//テキストフィールド表示用
	public void init() {
		fyear = new TextField("1992", 4);
		add(fyear);
		add(new Label("年"));
		fmonth = new TextField("7", 2);
		add(fmonth);
		add(new Label("月"));
		fday = new TextField("22", 2);
		add(fday);
		add(new Label("日"));
		fcalcdays = new TextField("60", 4);
		add(fcalcdays);
		add(new Label("日分"));
		button = new Button("描画");
		button.addActionListener(this);
		add(button);
		addMouseMotionListener(this);
		if (xOffset < 0 || yOffset < 0 || yLen <= 0 || xLen <= 0 || days <= 0 || bioLen.length <= 0) {
			illegalValueError = true;
		}
	}

	//マウス入力検知
	//ポインタがグラフ表示領域に入ってたらリペイント
	public void mouseMoved(MouseEvent e){
		isEnter = area.contains(e.getPoint());
		xPoint = e.getX();
		yPoint = e.getY();
		if(isEnter)
			repaint();
	}
	//空
	public void mouseDragged(MouseEvent e){}

	//計算ボタン押した時用の処理
	public void actionPerformed(ActionEvent a) {
		try {
			days = Integer.parseInt(fcalcdays.getText()); //グラフ表示時に使用するためこいつだけは代入しておく必要がある。
			Calendar cal1 = Calendar.getInstance();	//計算用
			Calendar cal2 = Calendar.getInstance();	//計算用
			cal1.set(Integer.parseInt(fyear.getText()), Integer.parseInt(fmonth.getText()) - 1, Integer.parseInt((fday.getText())));	//誕生日をぶちこむ
			passedDay = (int)((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (24 * 60 * 60 * 1000));	//経過日数をpassedDayに入れる
			//getTimeinMilliseで算出したミリ秒を（24時間60分60秒1000ミリ秒）で割って日数に変換してやるわけです。。
			//System.out.println(passedDay);
			if (passedDay > 0 && days > 0) {	//正常な値なら再描画を予約、そうでなければ止めとく
				isDraw = true;
			} else {
				illegalValueError = true;
			}
			area = new Rectangle(xOffset, yOffset, xLen * days, yLen); //日にちが変更されていた場合グラフ表示領域が伸縮するため再計算
			repaint();
		} catch (NumberFormatException e) {	//例外ブロック
			JFrame frame = new JFrame();
			Container container = frame.getContentPane();
			JOptionPane.showMessageDialog(container.getParent(), "まともな数字を入れてください。", "エラーやで", JOptionPane.WARNING_MESSAGE);
		}	
	}

	//描画メイン処理。
	//フラグが立っているかに基づいてイメージバッファを更新しつつ、メイン画面に転写し続ける。
	public void paint(Graphics g){
		if (illegalValueError) {
			JFrame frame = new JFrame();
			Container container = frame.getContentPane();
			JOptionPane.showMessageDialog(container.getParent(), "エラー！　コード埋め込みの数字がおかしいです", "エラーやで", JOptionPane.WARNING_MESSAGE);
			illegalValueError = false;
			return;
		}
		if (!pvInit) {	//最初に通るときのみグラフィックバッファを初期化し、かつグラフの軸を放り込んでおく
			size = getSize();
			back = createImage(size.width, size.height);
			buffer = back.getGraphics();
			prepareToDraw(g, xOffset, yOffset, xLen, yLen, days);
			pvInit = true;
		}
		if (isDraw) {	//グラフ処理フラグが立っている（ボタンを押した）時の処理
			size = getSize();
			back = createImage(size.width, size.height);
			buffer = back.getGraphics();
			prepareToDraw(g, xOffset, yOffset, xLen, yLen, days);
			g.drawImage(back, 0, 0, this);	//イメージバッファをカレントバッファに転写
			for (int i = 0; i < bioLen.length; i++) { //設定しただけの周期について、設定日分のバイオリズムを計算し、それを元にグラフを描画
				bio = calcBio(passedDay, bioLen[i], days);
				drawGraph(g, bio, i, xOffset, yOffset, xLen, yLen, days);
			}
			isDraw = false;
			graphInit = true;
		}
		if (isEnter && graphInit) {	//マウス位置に基づいてバイオリズム値を算出する
			int value = 0;
			buffer.clearRect(100, yOffset + yLen + 80, 200, 100 + 20 * bioLen.length);	//イメージバッファの該当領域を削除する。
			for (int i = 0; i < bioLen.length; i++) { //設定された周期分だけバイオリズムを（座標ベースで）計算
				value = (int)(100 * (Math.sin(Math.PI * 2 * (passedDay + (xPoint - xOffset) / xLen) / bioLen[i])));
				buffer.setColor(Color.black); //念の為
				buffer.drawString("" + value, 100, yOffset + yLen + 100 + 20 * i);
			}
		}
		g.drawImage(back, 0, 0, this);	//イメージバッファをカレントバッファに転写
	}

	public void update(Graphics g) { //clearRectさせないためにオーバーライドしておく。
		if (isEnter || isDraw) { //不要だが、このほうが早いと思われる。
			paint(g);
		}
	}

	//軸などを描画しておくメソッド
	void prepareToDraw(Graphics g, int xOffset, int yOffset, int xLen, int yLen, int days){
		//グラフの軸を描画
		buffer.drawLine(xOffset, yOffset, xOffset, yOffset + yLen);	//縦線
		buffer.drawLine(xOffset + xLen * (days - 1), yOffset, xOffset + xLen * (days - 1), yOffset + yLen); //反対側の縦線
		buffer.drawLine(xOffset, yOffset + (yLen / 2), xOffset + xLen * (days - 1), yOffset + (yLen / 2));	//横線
		//フォントの変更、状態表示
		setFont(new Font("Serif", Font.PLAIN, 11));
		buffer.drawString("ふつう", 0, yOffset + (yLen / 2));
		buffer.drawString("ごっつええ", 0, yOffset);
		buffer.drawString("あかん", 0, yOffset + yLen);
		//親切心
		buffer.drawString("身体", 30, yOffset + yLen + 100);
		buffer.drawString("感情", 30, yOffset + yLen + 120);
		buffer.drawString("知性", 30, yOffset + yLen + 140);
		buffer.setColor(Color.blue);
		buffer.drawLine(50, yOffset + yLen + 100, 100, yOffset + yLen + 100);
		buffer.setColor(Color.red);
		buffer.drawLine(50, yOffset + yLen + 120, 100, yOffset + yLen + 120);
		buffer.setColor(Color.green);
		buffer.drawLine(50, yOffset + yLen + 140, 100, yOffset + yLen + 140);
		buffer.setColor(Color.black);
	}

	//バイオリズム計算用メソッド
	//経過日数day,周期bioLen, 日数days
	double[] calcBio(int day, int bioLen, int days) {
		double[] bio = new double[days];
		for (int i = 0; i < days; i++) {	//60日分のバイオリズムを計算し、対応するbioのインデックスにねじ込む
			bio [i] = Math.sin((Math.PI * 2 * (day + i) )/ bioLen);
		}
		return bio;
	}

	//グラフ描画用メソッド
	void drawGraph (Graphics g, double[] bio,int graph, int xOffset, int yOffset, int xLen, int yLen, int days) {
		int yp, yv;
		yp = (int)((1-bio[0]) * (yLen / 2) + yOffset); //空だと困るので0日目（今日）のデータを入れる。
		switch (graph) {	//描くグラフの種類に応じて色が変動　bioLen増やしたらここも追加しとけ
		case 0 :
			g.setColor(Color.blue);
			buffer.setColor(Color.blue);
			break;
		case 1 :
			g.setColor(Color.red);
			buffer.setColor(Color.red);
			break;
		case 2 :
			g.setColor(Color.green);
			buffer.setColor(Color.green);
			break;
		}
		//59回線を引く。頂点は60になる。頂点がそれぞれの日におけるバイオリズムを示す。
		for (int i = 1; i < days; i++) {
			try { //アニメーション用
				Thread.sleep(5);
			} catch (InterruptedException e) {} //割り込みされることはないと思うが、一応
			yv = (int)((1-bio[i]) * (yLen / 2) + yOffset);
			g.drawLine(xOffset + xLen * (i - 1), yp, xOffset + i * xLen, yv);
			buffer.drawLine(xOffset + xLen * (i - 1), yp, xOffset + i * xLen, yv);
			yp = yv;
		}
		buffer.setColor(Color.black);	//一応
	}
}
