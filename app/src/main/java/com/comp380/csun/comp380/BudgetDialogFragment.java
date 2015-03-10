package com.comp380.csun.comp380;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gdfairclough on 3/10/15.
 */
public class BudgetDialogFragment extends DialogFragment {

    //private  mOnDialogResultListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //create a view for the dialog

        return container;

    }

    /* private OnDialogResultListener mOnDialogResultListener = null;
        public void setOnDialogResultListener(OnDialogResultListener dialogResultListener) {
            mOnDialogResultListener = dialogResultListener;
        }

        public static CustomDialog newInstance(OnDialogResultListener dialogResultListener) {
            CustomDialog frag = new CustomDialog();
            frag.setOnDialogResultListener(dialogResultListener);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getDialog().setTitle(getString(R.string.Dialog_CustomDialog_Title));
            View v = inflater.inflate(R.layout.customdialog, container, false);
            return v;
        }
        */
}

