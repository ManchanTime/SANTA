package com.gachon.santa.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gachon.santa.R;
import com.gachon.santa.activity.MainActivity;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PaintBoardFragment extends Fragment {

    private View view;
    private MyPaintView myView;
    private PaintBoard paintBoard;
    private Button btnTh, btnClear, btnSave, btnLoad, btnComplete;
    private ImageView btnRed, btnOrange, btnYellow, btnGreen, btnBlue, btnNavy, btnPurple, btnErase;
    private TextView notice;
    int thickness = 0;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private String path;
    private String cacheDir;
    private List<String> list = new ArrayList<>();
    private String name = "";
    private int count = 0;

    public PaintBoardFragment(String cacheDir, String path){
        this.cacheDir = cacheDir;
        this.path = path;
    }

    public PaintBoardFragment(String cacheDir, String path, List<String> list){
        this.cacheDir = cacheDir;
        this.path = path;
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_paint_board, container, false);

        myView = new MyPaintView(getActivity());
        paintBoard = new PaintBoard(myView, cacheDir);

        if(path.equals("figure")) {
            ((LinearLayout) getActivity().findViewById(R.id.paintLayout)).addView(myView);
        }
        else{
            ((LinearLayout) view.findViewById(R.id.paintLayout)).addView(myView);
        }
        btnTh = view.findViewById(R.id.btnTh);
        btnTh.setOnClickListener(onClickListener);
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        btnLoad = view.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(onClickListener);
        btnComplete = view.findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(onClickListener);
        if((TextView) getActivity().findViewById(R.id.notice) != null && list.size() != 0){
            notice = (TextView) getActivity().findViewById(R.id.notice);
            notice.setText(list.get(count));
        }
        if(list.size() != 0){
            btnComplete.setText("다음");
        }

        //색 버튼
        btnRed = view.findViewById(R.id.btn_red);
        btnRed.setOnClickListener(onClickListener);
        btnOrange = view.findViewById(R.id.btn_orange);
        btnOrange.setOnClickListener(onClickListener);
        btnYellow = view.findViewById(R.id.btn_yellow);
        btnYellow.setOnClickListener(onClickListener);
        btnGreen = view.findViewById(R.id.btn_green);
        btnGreen.setOnClickListener(onClickListener);
        btnBlue = view.findViewById(R.id.btn_blue);
        btnBlue.setOnClickListener(onClickListener);
        btnNavy = view.findViewById(R.id.btn_navy);
        btnNavy.setOnClickListener(onClickListener);
        btnPurple = view.findViewById(R.id.btn_purple);
        btnPurple.setOnClickListener(onClickListener);
        btnErase = view.findViewById(R.id.btn_erase);
        btnErase.setOnClickListener(onClickListener);

        return view;
    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch (v.getId()){
            case R.id.btnTh:
                if(thickness % 2 == 1){
                    btnTh.setText("Thin");
                    myView.mPaint.setStrokeWidth(10);
                } else {
                    btnTh.setText("Thick");
                    myView.mPaint.setStrokeWidth(20);
                }
                thickness++;
                break;
            case R.id.btnClear:
                paintBoard.clearPicture();
                break;
            case R.id.btnSave:
                paintBoard.savePicture(path);
                break;
            case R.id.btnLoad:
                paintBoard.loadPicture(path);
                break;
            case R.id.btnComplete:
                if(list.size() != 0) {
                    count++;
                    paintBoard.savePicture(path);
                    btnComplete.setText("다음");
                    notice.setText(list.get(count));
                    if (count == list.size() - 1) {
                        btnComplete.setText("저장");
                    }
                }else if(list.size() == count){
                    paintBoard.storeImage(path, path, user);
                }
                break;
            case R.id.btn_red:
                myView.mPaint.setColor(Color.RED);
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_orange:
                myView.mPaint.setColor(Color.parseColor("#ff7f00"));
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_yellow:
                myView.mPaint.setColor(Color.YELLOW);
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_green:
                myView.mPaint.setColor(Color.GREEN);
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_blue:
                myView.mPaint.setColor(Color.BLUE);
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_navy:
                myView.mPaint.setColor(Color.parseColor("#000080"));
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_purple:
                myView.mPaint.setColor(Color.parseColor("#8b00ff"));
                btnErase.setImageResource(R.drawable.erase);
                break;
            case R.id.btn_erase:
                myView.mPaint.setColor(Color.WHITE);
                btnErase.setImageResource(R.drawable.erase_click);
                break;
        }
    };
}