	import java.awt.*;
import java.applet.*;
import java.util.Calendar;

	//ゴールデンなウィークの課題用アプレット
	//sinグラフの部分は先生のやつをベースにさせていただきました

	//setFontの効果が画面を再描画するまで反映されてないようなんですがこれはそういうものなんでしょうか？

public class BioRhythmProto extends Applet{
	public void paint( Graphics g ){

		
		//-----------------------日付の計算------------------------
		//これ命令かなーって思ったら式中関数として使えたりもするの謎。これがオブジェクト指向なのか。
		//Calendarは日付の情報とかを保持できる型　宣言するときはgetInstanceとセット。これは変数に現在時刻をぶちこんでもくれる。
		//setは引数に指定した日付とかを変数に持たせる命令。今回はcal1に僕の誕生日をねじ込む。
		//getTimeInMillisは指定したcalendar変数を1970/1/1からの経過ミリ秒に変換する命令？　式中関数？　みたいなやつ
		//（日付-誕生日）で出たミリ秒を日数に直す。つまり(24時間＊60分＊60秒＊1000ミリ秒）で割ればいいから下のようになる
		Calendar cal1 = Calendar.getInstance();	//計算用
		Calendar cal2 = Calendar.getInstance();	//計算用
		cal1.set(1992, 7, 22);	//誕生日をぶちこむ
		long passedDay = ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (24 * 60 * 60 * 1000));	//経過日数をpassedDayに入れる
		g.drawString("経過日数　"+passedDay, 10, 10);	//念の為
		
		//------------------------変数の用意------------------------
		//ループを一度にしたいので基本三種類用意する。sがsensitive、iがintellectual、pがphysical
		//グラフの元のコードにあったdegreeは今回使用しない。for内でiを宣言して使う
		double sRh = Math.PI * 2 * passedDay / 28;	//リズム計算用
		double iRh = Math.PI * 2 * passedDay / 33;	
		double pRh = Math.PI * 2 * passedDay / 23;	
		int yps, yvs, ypi, yvi, ypp, yvp;	//yp:一個前のデータのy座標  yv:今のデータのy座標
		double values, valuei, valuep; 	//今のリズム値に対するsinの値
		double prevs, previ, prevp; 	//一つ前のsinの値
		prevs = Math.sin(sRh); 	//初期値をprevにぶち込む
		previ = Math.sin(iRh);
		prevp = Math.sin(pRh);
	
		//------------------------グラフ描きます-----------------------
		//ベースはアップしていただいたサイングラフのコードです
		
		//グラフの軸を描画
		g.drawLine(50, 50, 50, 150);	//縦線
		g.drawLine(50, 100, 410, 100);	//横線
		//フォントの変更、状態表示
		setFont(new Font("Serif", Font.PLAIN, 11));
		g.drawString("ふつう", 0, 105);
		g.drawString("ごっつええ", 0, 55);
		g.drawString("あかん", 0, 155);
		
		//親切心
		g.drawString("感情", 30, 200);
		g.drawString("知性", 30, 220);
		g.drawString("身体", 30, 240);
		g.setColor(Color.red);
		g.drawLine(50, 200, 100, 200);
		g.setColor(Color.green);
		g.drawLine(50, 220, 100, 220);
		g.setColor(Color.blue);
		g.drawLine(50, 240, 100, 240);
		
		//いよいよグラフの線を書いていく
		for( int i = 1; i <= 60; i += 1){ //60日分
			
			//経過日数がわかりやすいよう縦線を入れる。
			if (i % 20 == 0) {
				g.setColor(Color.black);
				g.drawString("" + i +"日後", 50 + i * 6 - 10, 50);
				g.drawLine(50 + i * 6, 50, 50 + i * 6, 150);
			}
			
			//過去の方のy座標算出
			yps = (int)(100 - 50 * prevs);	// 値:y座標　0:100 1:50 -1:150
			ypi = (int)(100 - 50 * previ);	// 値:y座標　0:100 1:50 -1:150
			ypp = (int)(100 - 50 * prevp);	// 値:y座標　0:100 1:50 -1:150
			
			//日数増やしてバイオリズム値再計算
			passedDay ++; 
			sRh = Math.PI * 2 * passedDay / 28;
			iRh = Math.PI * 2 * passedDay / 33;
			pRh = Math.PI * 2 * passedDay / 23;
			
			//今の経過日数に対するsin値を取得
			values = Math.sin(sRh); 	
			valuei = Math.sin(iRh); 	
			valuep = Math.sin(pRh);
			//Java標準のMath.sin()は引数をラジアンで指定
			//しなければならない
			//で、ラジアンって何ですか
			
			//現在の方のy座標算出
			yvs = (int)(100 - 50 * values);		// 値:y座標　0:100 1:50 -1:150
			yvi = (int)(100 - 50 * valuei);		// 値:y座標　0:100 1:50 -1:150
			yvp = (int)(100 - 50 * valuep);		// 値:y座標　0:100 1:50 -1:150

			//グラフ描画処理　センシ赤、フィジ青、イント緑
			g.setColor(Color.red);
			g.drawLine(50 + 6 * (i - 1), yps, 50 + i * 6, yvs);
			g.setColor(Color.green);
			g.drawLine(50 + 6 * (i - 1), ypi, 50 + i * 6, yvi);
			g.setColor(Color.blue);
			g.drawLine(50 + 6 * (i - 1), ypp, 50 + i * 6, yvp);
			
			//次にループするので現在変数の中身を過去変数に移動
			prevs = values;	
			previ = valuei;
			prevp = valuep;

		//ここまでループ
		}
	}
}

//オプションのやつの考え方　候補
//１．あらかじめpassedDayから30ひいとく（頭悪そう）
//２．マイナス方向に30いくループとプラス方向に30いくループをつくる（めんどい）
//３．iは回数を指してるんだから一つのループで+iと-iしてマイナス方向とプラス方向に行く（かっこいい！！！！！ 抱いて！！！！！！！！！！！！１１１１）

