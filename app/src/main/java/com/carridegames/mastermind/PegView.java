package com.carridegames.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.carridegames.mastermind.Game.Peg;

/**
 * Created by Tyler on 9/27/2014.
 */
public class PegView extends ImageView {

    protected Peg peg;
    protected int row;

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPeg(Peg.NONE);
    }


    public void setPeg(Peg peg) {
        this.peg = peg;
        this.setImageResource(peg.getDrawable(peg));
    }

    public Peg getPeg() {
        return peg;
    }


}
