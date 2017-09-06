package com.roy.quickapp.quickapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roy.quickapp.business.memo.MemoController;
import com.roy.quickapp.framework.AbstractController;

public class QuickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextManager.setActivityContext(this);
        AbstractController.init(this);
        initControllers();
    }

    private void initControllers(){
        InitController initController = new InitController(this);
        new MemoController(this);

        initController.start();
    }
}
