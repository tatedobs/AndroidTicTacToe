package edu.tatedobson.hhs.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class TicTacToe extends AppCompatActivity implements android.view.View.OnTouchListener{

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private boolean mGameOver;
    private int playerWins;
    private int ties;
    private int androidWins;
    private BoardView mBoardView;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    String mPlayerVictoryMessage;
    boolean mSoundOn = true;

    Handler handler = new Handler();

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mBoardView = (BoardView) findViewById(R.id.board);
        mGameOver = false;
        mGame = new TicTacToeGame();
        mInfoTextView = (TextView)findViewById(R.id.information);

        mPlayerVictoryMessage = getString(R.string.result_human_wins);

        startNewGame();

        // mPrefs stores preferences after app is closed
        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);


        // Setting all values equal to values stored in mPrefs
        playerWins = mPrefs.getInt("mHumanWins", 0);
        androidWins = mPrefs.getInt("mComputerWins", 0);
        ties = mPrefs.getInt("ties", 0);

        mGame.setDifficultyLevel(mPrefs.getInt("mDifficulty", 0));
        mGame.setBoardState(mPrefs.getString("board", "         ").toCharArray());
        mInfoTextView.setText(mPrefs.getString("info", "You go first"));

        mGameOver = mPrefs.getBoolean("mGameOver", false);


        // Refreshing win text, so it displays correct number
        TextView t = (TextView)findViewById(R.id.ties);
        t.setText(getString(R.string.ties) + ties);
        TextView h = (TextView)findViewById(R.id.human_wins);
        h.setText(getString(R.string.human) + playerWins);
        TextView a = (TextView)findViewById(R.id.android_wins);
        a.setText(getString(R.string.android) + androidWins);

    }

    public void startNewGame() {
        mBoardView.setOnTouchListener(this);
        mBoardView.setGame(mGame);
        mGame.clearBoard();
        mGameOver = false;
        mInfoTextView.setText(R.string.first_human);
        mBoardView.invalidate();
    }

    public boolean onTouch(View v, MotionEvent event) {
        // Determine which cell was touched
        int col = (int) event.getX() / mBoardView.getBoardCellWidth();
        int row = (int) event.getY() / mBoardView.getBoardCellHeight();
        int pos = row * 3 + col;

        if(mGame.getBoardOccupant(pos) == mGame.OPEN_SPOT && !mGameOver) {
            setMove(TicTacToeGame.HUMAN_PLAYER, pos);
            if(mGame.checkForWinner() == 0) {
                mGameOver = true;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setMove(TicTacToeGame.COMPUTER_PLAYER, mGame.getComputerMove());
                        mGameOver = false;
                    }
                }, 500);
            }
        }

        // So we aren't notified of continued events when finger is moved
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", playerWins);
        ed.putInt("mComputerWins", androidWins);
        ed.putInt("mTies", ties);

        ed.putInt("mDifficulty", mGame.getDifficultyLevel());
        ed.putBoolean("mGameOver", mGameOver);

        String s = new String(mGame.getBoardState());
        ed.putString("board", s);
        ed.putString("info", mInfoTextView.getText().toString());
        ed.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                break;
            case R.id.action_difficulty:
                DialogFragment newFragment = new DifficultyDialog();
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.clear_wins:
                playerWins = 0;
                androidWins = 0;
                ties = 0;
                startNewGame();
                // Refreshing win text, so it displays correct number
                TextView t = (TextView)findViewById(R.id.ties);
                t.setText(getString(R.string.ties) + ties);
                TextView h = (TextView)findViewById(R.id.human_wins);
                h.setText(getString(R.string.human) + playerWins);
                TextView a = (TextView)findViewById(R.id.android_wins);
                a.setText(getString(R.string.android) + androidWins);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.x_sound);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.o_sound);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = prefs.getBoolean("sound", true);
        mPlayerVictoryMessage = prefs.getString("victory_message", getString(R.string.victory_message));
        String diffLevel = prefs.getString("difficulty_level", getString(R.string.difficulty_harder));
    }

    private void setMove(char player, int location) {
        mBoardView.invalidate();
        mGame.setMove(player, location);

        if(mSoundOn) {
            if(player == TicTacToeGame.HUMAN_PLAYER) {
                mHumanMediaPlayer.start();
            } else {
                mComputerMediaPlayer.start();
            }
        }


        switch(mGame.checkForWinner()) {
            case 1:
                mInfoTextView.setText(R.string.result_tie);
                ties++;
                TextView t = (TextView)findViewById(R.id.ties);
                t.setText(getString(R.string.ties) + ties);
                mGameOver = true;
                break;
            case 2:
                mInfoTextView.setText(mPlayerVictoryMessage);
                playerWins++;
                TextView h = (TextView)findViewById(R.id.human_wins);
                h.setText(getString(R.string.human) + playerWins);
                mGameOver = true;
                break;
            case 3:
                mInfoTextView.setText(R.string.result_computer_wins);
                androidWins++;
                TextView a = (TextView)findViewById(R.id.android_wins);
                a.setText(getString(R.string.android) + androidWins);
                mGameOver = true;
                break;
        }
    }

    public TicTacToeGame getGame() {
        return mGame;
    }
}
