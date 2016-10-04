package com.tokenautocomplete;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class TokenActivity extends Activity implements TokenCompleteTextView.TokenListener<DataAPI> {
//    ContactsCompletionView completionView;

    ArrayList<DataAPI> people = new ArrayList<>();
    ArrayAdapter<DataAPI> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        people.add(new DataAPI("235261","Maria Magdalena"));
        people.add(new DataAPI("124455","Supri Wijaya"));
        people.add(new DataAPI("996518","Munir Ranto"));
        people.add(new DataAPI("332521","Karimun Oey"));
        people.add(new DataAPI("636714","Massayu Anastasia"));

        adapter = new FilteredArrayAdapter<DataAPI>(this, R.layout.person_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                DataAPI p = getItem(position);
                ((TextView)convertView.findViewById(R.id.idData)).setText(p.getId());
                ((TextView)convertView.findViewById(R.id.nameData)).setText(p.getNama());

                return convertView;
            }

            @Override
            protected boolean keepObject(DataAPI person, String mask) {
                mask = mask.toLowerCase();
                return person.getId().toLowerCase().startsWith(mask) || person.getNama().toLowerCase().startsWith(mask);
            }
        };

//        completionView = (ContactsCompletionView)findViewById(R.id.searchView);
//        completionView.setAdapter(adapter);
//        completionView.setTokenListener(this);
//        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);


//        if (savedInstanceState == null) {
//            completionView.setPrefix("To: ");
//            completionView.addObject(people[0]);
//            completionView.addObject(people[1]);
//        }

//        Button removeButton = (Button)findViewById(R.id.removeButton);
//        removeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<DataAPI> people = completionView.getObjects();
//                if (people.size() > 0) {
//                    completionView.removeObject(people.get(people.size() - 1));
//                }
//            }
//        });
//
//        Button addButton = (Button)findViewById(R.id.addButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random rand = new Random();
//                completionView.addObject(people[rand.nextInt(people.length)]);
//            }
//        });
    }

    private void updateTokenConfirmation() {
        StringBuilder sb = new StringBuilder("Current tokens:\n");
//        for (DataAPI token: completionView.getObjects()) {
//            sb.append(token.getId());
//            sb.append("\n");
//        }

        ((TextView)findViewById(R.id.tokens)).setText(sb);
    }


    @Override
    public void onTokenAdded(DataAPI token) {
//        ((TextView)findViewById(R.id.lastEvent)).setText("Added: " + token);
        updateTokenConfirmation();
    }
    @Override
    public void onTokenRemoved(DataAPI token) {
//        ((TextView)findViewById(R.id.lastEvent)).setText("Removed: " + token);
        updateTokenConfirmation();
    }
}
