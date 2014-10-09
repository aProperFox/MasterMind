package com.carridegames.mastermind;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.carridegames.mastermind.Game.Peg;
import com.carridegames.mastermind.Game.WinState;

import java.util.ArrayList;

/**
 * Created by Tyler on 9/17/2014.
 */
public class GameScreen extends Activity {

    protected PegView currentEditPeg;
    protected TableRow currentRow;

    protected ArrayList<Peg> guess;

    protected Game gameLogic;

    Display display;
    Point size;
    int screenWidth, screenHeight;

    private Dialog endDialog;
    private static View endView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.game_layout);

        initializeGame();

        // Set display variables
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // Layout inflater to help inflate a view for the dialog when the game is over
        LayoutInflater inflater = LayoutInflater.from(GameScreen.this);
        endView = inflater.inflate(R.layout.dialog_game_over, null);
        endView.setMinimumWidth( (int)(screenWidth * 0.9f) );
        endView.setMinimumHeight( (int)(screenHeight * 0.6f) );


        // Instantiate the end dialog for when the game is over
        endDialog = new Dialog( GameScreen.this, android.R.style.Theme_Translucent );
        endDialog.getWindow().setLayout((int) (screenWidth * 0.9f), screenHeight / 2);
        endDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        endDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        endDialog.setContentView(endView);
        endDialog.getWindow().getAttributes().dimAmount = 0.5f;

        endDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return true;
            }
        });

    }

    private void initializeGame() {

        gameLogic = new Game(getSharedPreferences(MainMenu.PREFERENCES, 0).getBoolean("allowRepeats", false));

        guess = new ArrayList<Peg>(4);

        PegView pegView = (PegView)findViewById(R.id.peg_black);
        pegView.setPeg(Peg.BLACK);

        pegView = (PegView)findViewById(R.id.peg_gray);
        pegView.setPeg(Peg.GRAY);

        pegView = (PegView)findViewById(R.id.peg_blue);
        pegView.setPeg(Peg.BLUE);

        pegView = (PegView)findViewById(R.id.peg_red);
        pegView.setPeg(Peg.RED);

        pegView = (PegView)findViewById(R.id.peg_green);
        pegView.setPeg(Peg.GREEN);

        pegView = (PegView)findViewById(R.id.peg_orange);
        pegView.setPeg(Peg.ORANGE);

        pegView = (PegView)findViewById(R.id.peg_purple);
        pegView.setPeg(Peg.PURPLE);

        pegView = (PegView)findViewById(R.id.peg_yellow);
        pegView.setPeg(Peg.YELLOW);

        // Initialize the first peg when starting the app
        currentEditPeg = (PegView)findViewById(R.id.peg_0);
        currentRow = (TableRow)findViewById(R.id.row_0);

        guess.add(Peg.NONE);
        guess.add(Peg.NONE);
        guess.add(Peg.NONE);
        guess.add(Peg.NONE);
    }

    public void setCurrentEditPeg(View view) {
        // The PegView of what was clicked
        PegView pegView = (PegView)view;

        // Iterate through current row to find if what was clicked is legal
        for (int i = 0; i < currentRow.getChildCount(); i++) {
            if (currentRow.getChildAt(i).getId() == pegView.getId()) {
                currentEditPeg = pegView;
                return;
            }
        }
    }

    public void onPegClick(View view) {
        // The PegView that was clicked
        PegView pv = (PegView)view;

        // Cache the clicked peg
        Peg peg = pv.getPeg();

        // Set the current peg (color) to what was clicked
        currentEditPeg.setPeg(peg);

        // Cache resources
        Resources resources = getResources();

        // Get the name of the resource for the current peg
        String currentPegName = resources.getResourceEntryName(currentEditPeg.getId());

        int currentPegId = Integer.parseInt(currentPegName.substring(currentPegName.indexOf('_') + 1));

        guess.set(currentPegId % 4, peg);

        // Get the ID of the current peg and increment the number by 1, then store
        int newPegId;

        // If we're at the end of the row, reset to the beginning
        if(currentPegId % 4 == 3)
            newPegId = currentPegId - 3;
        else
            newPegId = currentPegId + 1;

        // Set the name of the new peg resource id
        String newPegName = "peg_" + newPegId;

        // Set the new Peg
        currentEditPeg = (PegView)findViewById(resources.getIdentifier(newPegName,"id",getPackageName()));
    }

    public void guessRow(View view) {

        if(guess.contains(Peg.NONE)) {
            Context context = getApplicationContext();
            CharSequence text = "Please select 4 colors.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        } else if(!gameLogic.areRepeatsAllowed) {
            for(int i = 0; i < 3; i++) {
                for(int j = i + 1; j < 4; j++) {
                    if(guess.get(i).equals(guess.get(j))) {
                        Context context = getApplicationContext();
                        CharSequence text = "Repeats are not allowed.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }
                }
            }
        } else {

        }

        //------ Set new row and peg ------\\

        // Cache resources
        Resources resources = getResources();

        String currentRowName = resources.getResourceEntryName(currentRow.getId());

        int newRowId = Integer.parseInt(currentRowName.substring(currentRowName.indexOf('_') + 1)) + 1;


        WinState winState = gameLogic.checkAnswer(guess);

        ImageView stateImage = new ImageView(this);
        stateImage = (ImageView) currentRow.getChildAt(currentRow.getChildCount() - 1);
        stateImage = (ImageView) findViewById(stateImage.getId());
        stateImage.setImageResource(getResources().getIdentifier("reds" + winState.getReds() + "whites" + winState.getWhites(), "drawable", getPackageName()));


        if(winState.getReds() == 4) {
            showDialog(true);
            return;
        } else {

            if(newRowId > 9) {
                showDialog(false);
                return;
            }

            String newPegName = "peg_" + (newRowId * 4);


            // Set the new Peg
            currentEditPeg = (PegView)findViewById(resources.getIdentifier(newPegName,"id",getPackageName()));

            // Set the name of the new peg resource id
            String newRowName = "row_" + newRowId;

            // Set the new Peg
            currentRow = (TableRow)findViewById(resources.getIdentifier(newRowName,"id",getPackageName()));

            guess.set(0, Peg.NONE);
            guess.set(1, Peg.NONE);
            guess.set(2, Peg.NONE);
            guess.set(3, Peg.NONE);
        }



    }

    protected void showDialog(boolean wonGame) {
        String endText = "";
        if(wonGame) {
            endText = "Nice work, you win!";
        } else {
            endText = "Sorry, you lose :(";
        }

        TextView tv = (TextView)endView.findViewById(R.id.game_over_text);
        tv.setText(endText);

        PegView pegView = (PegView) endView.findViewById(R.id.end_peg_0);
        pegView.setPeg(gameLogic.answer.get(0));
        pegView = (PegView) endView.findViewById(R.id.end_peg_1);
        pegView.setPeg(gameLogic.answer.get(1));
        pegView = (PegView) endView.findViewById(R.id.end_peg_2);
        pegView.setPeg(gameLogic.answer.get(2));
        pegView = (PegView) endView.findViewById(R.id.end_peg_3);
        pegView.setPeg(gameLogic.answer.get(3));

        endDialog.show();
    }

    public void newGame(View view) {
        endDialog.cancel();
        setContentView(R.layout.activity_game_screen);
        initializeGame();
    }

    public void mainMenu(View view) {
        Intent i = new Intent(GameScreen.this, MainMenu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        endDialog.cancel();

        startActivity(i);
    }

}
