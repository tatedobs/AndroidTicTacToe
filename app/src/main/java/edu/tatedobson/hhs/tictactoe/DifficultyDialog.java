package edu.tatedobson.hhs.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class DifficultyDialog extends DialogFragment {
    private TicTacToeGame mGame;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        mGame = ((TicTacToe) getActivity()).getGame();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.difficulty_choose);
        final CharSequence[] levels = {
                getResources().getString(R.string.difficulty_easy),
                getResources().getString(R.string.difficulty_harder),
                getResources().getString(R.string.difficulty_expert)};
        int selected = mGame.getDifficultyLevel();

        // selected is the radio button that is selected when the Dialog Box appears.
        builder.setSingleChoiceItems(levels, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss(); // Close dialog
                        if(mGame.getDifficultyLevel() == item) {
                            return;
                        }
                        mGame.setDifficultyLevel(item);
                        ((TicTacToe)getActivity()).startNewGame();
                    }
                });
        dialog = builder.create();
        return dialog;
    }
}