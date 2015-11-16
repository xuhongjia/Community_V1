package cn.andrewlu.community.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class TipRadioButton extends RadioButton {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private String tipString = null;
	private final static int tipWidth = 20;

	public TipRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public TipRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public void setTip(String tip) {
		this.tipString = tip;
		if (tip == null) {
		} else if (tipString.length() >= 3) {
			tipString = "...";
		}
		postInvalidate();
	}

	private void init() {
		mPaint.setColor(Color.RED);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTextSize(tipWidth);
	}

	private void onDrawTip(Canvas canvas) {
		canvas.save();
		do {
			if (tipString == null) {
				break;
			}

			mPaint.setColor(Color.RED);

			int width = getWidth();

			if (tipString.equals("")) {
				canvas.drawCircle(width * 0.65f, tipWidth, tipWidth / 2, mPaint);
			} else {
				canvas.drawCircle(width * 0.65f, tipWidth, tipWidth - 2, mPaint);

				mPaint.setColor(Color.WHITE);

				Rect rect = new Rect();
				mPaint.getTextBounds(tipString, 0, tipString.length(), rect);

				canvas.drawText(tipString, width * 0.65f,
						tipWidth * 2 - rect.height() / 2.0f, mPaint);
			}

		} while (false);

		canvas.restore();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		onDrawTip(canvas);
	}
}
