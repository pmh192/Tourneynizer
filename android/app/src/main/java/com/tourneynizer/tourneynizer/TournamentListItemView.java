package com.tourneynizer.tourneynizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class TournamentListItemView extends View {
    private Paint textPaint;
    private String tournamentName;

    public TournamentListItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public TournamentListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TournamentListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TournamentListItemView, defStyle, 0);
        tournamentName = a.getString(R.styleable.TournamentListItemView_tournamentName);
        if (tournamentName == null) {
            tournamentName = "POOP";
        }
        a.recycle();
        updateView();
    }

    private void updateView() {
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(tournamentName, 0, 0, textPaint);
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournaentName(String tournament) {
        tournamentName = tournament;
        updateView();
    }
}
