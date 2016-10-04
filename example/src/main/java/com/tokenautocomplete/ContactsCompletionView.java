package com.tokenautocomplete;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import static java.security.AccessController.getContext;

/**
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView<DataAPI> {

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(DataAPI person) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        TextView token = (TextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
//        TextView token = (TextView) l.inflate(R.layout.activity_main, (ViewGroup) getParent(), false);
//        TextView token = (TextView) findViewById(R.id.searchView);
        token.setText(person.getNama());
        return token;
    }

    @Override
    protected DataAPI defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new DataAPI(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new DataAPI(completionText.substring(0, index), completionText);
        }
    }
}
